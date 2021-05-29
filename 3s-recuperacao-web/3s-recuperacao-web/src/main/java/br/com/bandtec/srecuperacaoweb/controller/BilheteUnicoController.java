package br.com.bandtec.srecuperacaoweb.controller;

import br.com.bandtec.srecuperacaoweb.model.BilheteUnico;
import br.com.bandtec.srecuperacaoweb.repository.BilheteUnicoRepository;
import br.com.bandtec.srecuperacaoweb.repository.TipoPassagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bilhetes-unicos")
public class BilheteUnicoController {

    @Autowired
    private BilheteUnicoRepository bilheteUnicoRepository;

    @Autowired
    private TipoPassagemRepository tipoPassagemRepository;

    @GetMapping
    public ResponseEntity getBilhetesUnicos(){
        List<BilheteUnico> bilhetesUnicos = bilheteUnicoRepository.findAll();
        if (!bilhetesUnicos.isEmpty()){
            return ResponseEntity.status(200).body(bilhetesUnicos);

        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PostMapping
    public ResponseEntity postBilheteUnico(@RequestBody @Valid BilheteUnico novoBilheteUnico){
        List<BilheteUnico> bilhetesUnicos = bilheteUnicoRepository.buscaPorCpf(novoBilheteUnico.getCpf());
        if (bilhetesUnicos.size() < 2){
            bilheteUnicoRepository.save(novoBilheteUnico);
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(400).body("Este CPF já tem 2 BUs!");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity getBilhetesUnicos(@PathVariable int id){
        if (bilheteUnicoRepository.existsById(id)){
            return ResponseEntity.status(200).body(bilheteUnicoRepository.findById(id));

        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{id}/recarga/{valorRecarga}")
    public ResponseEntity recarga(@PathVariable int id, @PathVariable Double valorRecarga){
        if (bilheteUnicoRepository.existsById(id)){
            if (valorRecarga < 1.00){
                return ResponseEntity.status(400).body("Valor da recarga deve ser a partir de R$1,00");

            } else if(bilheteUnicoRepository.getById(id).getSaldo() + valorRecarga > 230.0){
                double X = 230.0 - bilheteUnicoRepository.getById(id).getSaldo();
                return ResponseEntity.status(400).body(
                    "Recarga não efetuada! Passaria do limite de R$230,00. Você ainda pode carregar até R$" + X);

            } else {
                BilheteUnico bilheteUnicoUpdate = bilheteUnicoRepository.getById(id);
                bilheteUnicoUpdate.setSaldo(bilheteUnicoUpdate.getSaldo() + valorRecarga);
                bilheteUnicoRepository.save(bilheteUnicoUpdate);
                return ResponseEntity.status(201).build();

            }
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/{id}/passagem/{idTipo}")
    public ResponseEntity passagem(@PathVariable int id, @PathVariable int idTipo){
        if (!bilheteUnicoRepository.existsById(id)){
            return ResponseEntity.status(404).body("BU não encontrado");

        } else if (!tipoPassagemRepository.existsById(idTipo)){
            return ResponseEntity.status(404).body("Tipo de passagem não encontrada");

        } else if (bilheteUnicoRepository.getById(id).getSaldo() < tipoPassagemRepository.getById(idTipo).getValor()){
            return ResponseEntity.status(400).body("Saldo atual (R$"
                    +bilheteUnicoRepository.getById(id).getSaldo()+") insuficiente para esta passagem");

        } else {
            BilheteUnico bilheteUnicoUpdate = bilheteUnicoRepository.getById(id);
            bilheteUnicoUpdate.setSaldo(bilheteUnicoUpdate.getSaldo() - tipoPassagemRepository.getById(idTipo).getValor());
            bilheteUnicoRepository.save(bilheteUnicoUpdate);
            return ResponseEntity.status(201).build();
        }
    }
}
