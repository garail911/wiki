package tech.itpark.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import tech.itpark.exception.DataAccessException;
import tech.itpark.jdbc.JdbcTemplate;

import tech.itpark.model.Wiki;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class WikiRepositoryImpl implements WikiRepository {
    private final DataSource ds;
    private final JdbcTemplate template = new JdbcTemplate();

    @Override
    public List<Wiki> getAll() {
        List<Wiki> result = new ArrayList<>();
        try (
                final var conn = ds.getConnection();
                final var stmt = conn.prepareStatement("""
                        SELECT id, author_id, person, content FROM wiki WHERE id = ?
                        """);
        ) {
            final var rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(new Wiki(
                        rs.getLong("id"),
                        rs.getLong("authorId"),
                        rs.getString("person"),
                        rs.getString("content")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    @Override
    public Wiki create(Wiki wiki) {
        try (
                final var conn = ds.getConnection();
                final var stmt = conn.prepareStatement("""
                        INSERT INTO wiki(author_id, person, content) VALUES (?, ?, ?);
                        """, Statement.RETURN_GENERATED_KEYS);
        ) {
            var index = 0;
            stmt.setLong(++index, wiki.getAuthorId());
            stmt.setString(++index, wiki.getPerson());
            stmt.setString(++index, wiki.getContent());
            stmt.executeUpdate();

            final var keys = stmt.getGeneratedKeys();
            if (!keys.next()) {
                throw new DataAccessException("no keys in result");
            }
            wiki.setId(keys.getLong(1));

            return wiki;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Wiki update(Wiki wiki) {
        try (
                final var stmt = ds.getConnection().prepareStatement("UPDATE wiki SET author_id = ?, person = ?, content = ? WHERE id = ?")
        ) {
            var index = 0;
            stmt.setLong(++index, wiki.getAuthorId());
            stmt.setString(++index, wiki.getPerson());
            stmt.setString(++index, wiki.getContent());
            stmt.setLong(++index, wiki.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
        return wiki;
    }

    @Override
    public void remove(Long id, Boolean removed) {
        try (
                final var stmt = ds.getConnection().prepareStatement("UPDATE users SET removed = ? WHERE id = ?")
        ) {
            var index = 0;
            stmt.setBoolean(++index, removed);
            stmt.setLong(++index, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
