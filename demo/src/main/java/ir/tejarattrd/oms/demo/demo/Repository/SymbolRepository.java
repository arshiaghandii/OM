package ir.tejarattrd.oms.demo.demo.Repository;
import ir.tejarattrd.oms.demo.demo.Entity.Symbol;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
/*-------------------------------------------------------------------------------- */
@Repository
public class SymbolRepository {

    private final JdbcTemplate jdbcTemplate;

    public SymbolRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final class SymbolRowMapper implements RowMapper<Symbol> {
        @Override
        public Symbol mapRow(ResultSet rs, int rowNum) throws SQLException {
            Symbol s = new Symbol();
            s.setSymbolId(rs.getLong("symbol_id"));
            s.setSymbolCode(rs.getString("symbol_code"));
            s.setCompanyName(rs.getString("company_name"));
            s.setMarket(rs.getString("market"));
            s.setUnitPrice(rs.getDouble("unit_price"));
            return s;
        }
    }

    public List<Symbol> findAll() {
        String sql = "SELECT * FROM symbols";
        return jdbcTemplate.query(sql, new SymbolRowMapper());
    }

    public Optional<Symbol> findById(Long id) {
        String sql = "SELECT * FROM symbols WHERE symbol_id = ?";
        List<Symbol> list = jdbcTemplate.query(sql, new SymbolRowMapper(), id);
        return list.stream().findFirst();
    }

    public Optional<Symbol> findBySymbolCode(String code) {
        String sql = "SELECT * FROM symbols WHERE symbol_code = ?";
        List<Symbol> list = jdbcTemplate.query(sql, new SymbolRowMapper(), code);
        return list.stream().findFirst();
    }

    public int save(Symbol symbol) {
        String sql = "INSERT INTO symbols (symbol_code, company_name, market, unit_price) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, symbol.getSymbolCode(), symbol.getCompanyName(), symbol.getMarket(), symbol.getUnitPrice());
    }

    public int update(Symbol symbol) {
        String sql = "UPDATE symbols SET company_name = ?, market = ?, unit_price    = ? WHERE symbol_id = ?";
        return jdbcTemplate.update(sql, symbol.getCompanyName(), symbol.getMarket(), symbol.getUnitPrice(), symbol.getSymbolId());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM symbols WHERE symbol_id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
