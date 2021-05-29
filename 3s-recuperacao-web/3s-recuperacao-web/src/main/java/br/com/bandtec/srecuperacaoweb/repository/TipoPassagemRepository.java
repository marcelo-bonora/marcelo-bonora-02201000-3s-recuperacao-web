package br.com.bandtec.srecuperacaoweb.repository;

import br.com.bandtec.srecuperacaoweb.model.BilheteUnico;
import br.com.bandtec.srecuperacaoweb.model.TipoPassagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoPassagemRepository extends JpaRepository<TipoPassagem, Integer> {

    @Query("SELECT t FROM TipoPassagem t WHERE t.descricao like ?1")
    List<TipoPassagem> descricaoExist(String decricao);

}
