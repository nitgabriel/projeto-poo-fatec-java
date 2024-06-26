package br.com.fatec.DAO;

import br.com.fatec.model.Unidade;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class UnidadeDAO implements DAO<Unidade> {

    private Unidade unidade;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Unidade model) throws SQLException {
        String sql = "INSERT INTO Unidades (nome, cep, rua, bairro, cidade, uf, numero) VALUES (?, ?, ?, ?, ?, ?, ?);";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setInt(2, model.getCep());
        pst.setString(3, model.getRua());
        pst.setString(4, model.getBairro());
        pst.setString(5, model.getCidade());
        pst.setString(6, model.getUf());
        pst.setString(7, model.getNumero());
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
        String sql = "UPDATE Unidades SET nome = ?, cep = ?, rua = ?, bairro = ?, cidade = ?, uf = ?, numero = ? WHERE idUnidade = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setInt(2, model.getCep());
        pst.setString(3, model.getRua());
        pst.setString(4, model.getBairro());
        pst.setString(5, model.getCidade());
        pst.setString(6, model.getUf());
        pst.setString(7, model.getNumero());
        pst.setInt(8, model.getIdUnidade());
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
        unidade = null;
        if (rs.next()) {
            unidade = new Unidade(
                    rs.getInt("idUnidade"), 
                    rs.getString("nome"), 
                    rs.getInt("cep"),
                    rs.getString("rua"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("uf"),
                    rs.getString("numero"));
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
            unidade = new Unidade(
                    rs.getInt("idUnidade"), 
                    rs.getString("nome"), 
                    rs.getInt("cep"),
                    rs.getString("rua"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("uf"),
                    rs.getString("numero"));
            listagem.add(unidade);
        }
        Banco.desconectar();
        return listagem;
    }

    //MÉTODO APENAS PARA SETAR O PRÓXIMO ID A SER CADASTRADO NA TABELA.
    //ASSIM O USUÁRIO SABE QUAL SERÁ O PRÓXIMO ID, E OS ANTERIORES SERVEM
    //PARA CONSULTA, ALTERAÇÃO E DELETAR.
    public int getNextId() throws SQLException {
        String sql = "SELECT COALESCE(MAX(idUnidade), 0) + 1 AS nextId FROM Unidades";
        // COALESCE PARA TRATAR O CASO DE POSSÍVEL RETORNO DE 0 PELA SOMA DE NULL + 1 = 0/NULL

        Banco.conectar();
        Statement stmt  = Banco.getConexao().createStatement();
        ResultSet rs    = stmt.executeQuery(sql);

        if (rs.next()) {
            return rs.getInt("nextId");
        } else {
            return 1;  // Se a tabela estiver vazia, retorna 1
        }
    }
}
