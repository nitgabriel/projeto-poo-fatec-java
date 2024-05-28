package br.com.fatec.DAO;

import br.com.fatec.persistencia.Banco;
import br.com.fatec.model.Dono;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class DonoDAO implements DAO<Dono> {

    private Dono dono;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Dono model) throws SQLException {
        String sql = "INSERT INTO Donos (nome, email, cpf, formaPagamento, contato) VALUES (?, ?, ?, ?, ?);";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getEmail());
        pst.setString(3, model.getCpf());
        pst.setString(4, model.getFormaPagamento());
        pst.setString(5, model.getContato());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean remove(Dono model) throws SQLException {
        String sql = "DELETE FROM Donos WHERE idDono = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdDono());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean altera(Dono model) throws SQLException {
        String sql = "UPDATE Donos SET nome = ?, email = ?, cpf = ?, formaPagamento = ?, contato = ? WHERE idDono = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getEmail());
        pst.setString(3, model.getCpf());
        pst.setString(4, model.getFormaPagamento());
        pst.setString(5, model.getContato());
        pst.setInt(6, model.getIdDono());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public Dono buscaID(Dono model) throws SQLException {
        String sql = "SELECT * FROM Donos WHERE idDono = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdDono());
        rs = pst.executeQuery();
        dono = null;
        if (rs.next()) {
            dono = new Dono(rs.getInt("idDono"), rs.getString("nome"), rs.getString("email"), rs.getString("cpf"), rs.getString("formaPagamento"), rs.getString("contato"));
        }
        Banco.desconectar();
        return dono;
    }

    @Override
    public Collection<Dono> lista(String criterio) throws SQLException {
        Collection<Dono> listagem = new ArrayList<>();
        String sql = "SELECT * FROM Donos";
        if (!criterio.isEmpty()) {
            sql += " WHERE " + criterio;
        }
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            dono = new Dono(rs.getInt("idDono"), rs.getString("nome"), rs.getString("email"), rs.getString("cpf"), rs.getString("formaPagamento"), rs.getString("contato"));
            listagem.add(dono);
        }
        Banco.desconectar();
        return listagem;
    }

    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(idDono), 0) + 1 AS nextId FROM Donos;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        int nextId = 1; // VALOR PADRÃO
        if (rs.next()) {
            nextId = rs.getInt("nextId");
        }
        Banco.desconectar();
        return nextId;
    }
}
