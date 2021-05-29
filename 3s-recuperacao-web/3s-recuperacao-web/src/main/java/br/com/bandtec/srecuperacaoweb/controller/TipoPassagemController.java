package br.com.bandtec.srecuperacaoweb.controller;

import br.com.bandtec.srecuperacaoweb.model.BilheteUnico;
import br.com.bandtec.srecuperacaoweb.model.TipoPassagem;
import br.com.bandtec.srecuperacaoweb.repository.TipoPassagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tipos-passagens")
public class TipoPassagemController {

    @Autowired
    private TipoPassagemRepository repository;

    @GetMapping
    public ResponseEntity getTiposPassagens(){
        List<TipoPassagem> tiposPassagens = repository.findAll();
        if (!tiposPassagens.isEmpty()){
            return ResponseEntity.status(200).body(tiposPassagens);

        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @PostMapping
    public ResponseEntity postTipoPassagem(@RequestBody @Valid TipoPassagem novoTipoPassagem){
        List<TipoPassagem> tiposPassagens = repository.descricaoExist(novoTipoPassagem.getDescricao());
        if (tiposPassagens.isEmpty()){
            repository.save(novoTipoPassagem);
            return ResponseEntity.status(201).build();

        } else {
            return ResponseEntity.status(400).body("Este tipo de passagem já está cadastrado!");
        }
    }

}
