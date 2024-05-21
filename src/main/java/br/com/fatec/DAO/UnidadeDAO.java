package br.com.fatec.DAO;

import br.com.fatec.model.Unidade;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class UnidadeDAO implements DAO<Unidade> {

    private Unidade unidade;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Unidade model) throws SQLException {
        String sql = "INSERT INTO Unidades (nome, endereco) VALUES (?, ?);";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getEndereco());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean remove(Unidade model) throws SQLException {
        String sql = "DELETE FROM Unidades WHERE idUnidade = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdUnidade());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean altera(Unidade model) throws SQLException {
        String sql = "UPDATE Unidades SET nome = ?, endereco = ? WHERE idUnidade = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getEndereco());
        pst.setInt(3, model.getIdUnidade());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public Unidade buscaID(Unidade model) throws SQLException {
        String sql = "SELECT * FROM Unidades WHERE idUnidade = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdUnidade());
        rs = pst.executeQuery();
        if (rs.next()) {
            unidade = new Unidade(rs.getInt("idUnidade"), rs.getString("nome"), rs.getString("endereco"));
        }
        Banco.desconectar();
        return unidade;
    }

    @Override
    public Collection<Unidade> lista(String criterio) throws SQLException {
        Collection<Unidade> listagem = new ArrayList<>();
        String sql = "SELECT * FROM Unidades";
        if (!criterio.isEmpty()) {
            sql += " WHERE " + criterio;
        }
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            unidade = new Unidade(rs.getInt("idUnidade"), rs.getString("nome"), rs.getString("endereco"));
            listagem.add(unidade);
        }
        Banco.desconectar();
        return listagem;
    }
}
