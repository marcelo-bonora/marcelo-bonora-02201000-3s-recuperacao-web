package br.com.bandtec.srecuperacaoweb.repository;

import br.com.bandtec.srecuperacaoweb.model.BilheteUnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BilheteUnicoRepository extends JpaRepository<BilheteUnico, Integer> {

    @Query("SELECT b FROM BilheteUnico b WHERE b.cpf like ?1")
    List<BilheteUnico> buscaPorCpf(String cpf);

}
