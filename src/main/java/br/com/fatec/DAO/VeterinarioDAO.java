package br.com.fatec.DAO;

import br.com.fatec.model.Veterinario;
import br.com.fatec.model.Unidade;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class VeterinarioDAO implements DAO<Veterinario> {

    private Veterinario veterinario;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Veterinario model) throws SQLException {
        String sql = "INSERT INTO Veterinarios (nome, crmv, idUnidade, status, especialidade) VALUES (?, ?, ?, ?, ?);";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getCrmv());
        pst.setInt(3, model.getUnidade().getIdUnidade());
        pst.setString(4, model.getStatus());
        pst.setString(5, model.getEspecialidade());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean remove(Veterinario model) throws SQLException {
        String sql = "DELETE FROM Veterinarios WHERE idVeterinario = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdVeterinario());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean altera(Veterinario model) throws SQLException {
        String sql = "UPDATE Veterinarios SET nome = ?, crmv = ?, idUnidade = ?, status = ?, especialidade = ? WHERE idVeterinario = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getCrmv());
        pst.setInt(3, model.getUnidade().getIdUnidade());
        pst.setString(4, model.getStatus());
        pst.setString(5, model.getEspecialidade());
        pst.setInt(6, model.getIdVeterinario());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public Veterinario buscaID(Veterinario model) throws SQLException {
        String sql = "SELECT * FROM Veterinarios WHERE idVeterinario = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdVeterinario());
        rs = pst.executeQuery();
        if (rs.next()) {
            UnidadeDAO unidadeDAO = new UnidadeDAO();
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(rs.getInt("idUnidade"));
            unidade = unidadeDAO.buscaID(unidade);
            
            veterinario = new Veterinario(rs.getInt("idVeterinario"), rs.getString("nome"), rs.getString("crmv"), unidade, rs.getString("status"), rs.getString("especialidade"));
        }
        Banco.desconectar();
        return veterinario;
    }

    @Override
    public Collection<Veterinario> lista(String criterio) throws SQLException {
        Collection<Veterinario> listagem = new ArrayList<>();
        String sql = "SELECT * FROM Veterinarios";
        if (!criterio.isEmpty()) {
            sql += " WHERE " + criterio;
        }
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            UnidadeDAO unidadeDAO = new UnidadeDAO();
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(rs.getInt("idUnidade"));
            unidade = unidadeDAO.buscaID(unidade);
            
            veterinario = new Veterinario(rs.getInt("idVeterinario"), rs.getString("nome"), rs.getString("crmv"), unidade, rs.getString("status"), rs.getString("especialidade"));
            listagem.add(veterinario);
        }
        Banco.desconectar();
        return listagem;
    }

    public Veterinario buscaPorCrmv(String crmv) throws SQLException {
        String sql = "SELECT * FROM Veterinarios WHERE crmv = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, crmv);
        rs = pst.executeQuery();
        Veterinario veterinario = null;
        if (rs.next()) {
            UnidadeDAO unidadeDAO = new UnidadeDAO();
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(rs.getInt("idUnidade"));
            unidade = unidadeDAO.buscaID(unidade);
            veterinario = new Veterinario(rs.getInt("idVeterinario"), rs.getString("nome"), rs.getString("crmv"), unidade, rs.getString("status"), rs.getString("especialidade"));
        }
        Banco.desconectar();
        return veterinario;
    }
}
