package tech.itpark.repository;

import tech.itpark.model.Wiki;

import java.util.List;

public interface WikiRepository {
    List<Wiki> getAll();

    Wiki create(Wiki wiki);

    Wiki update(Wiki wiki);

    void remove(Long id, Boolean removed);
}
