package br.com.fatec.DAO;

import br.com.fatec.model.Dono;
import br.com.fatec.model.Pet;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class PetDAO implements DAO<Pet> {

    private Pet pet;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Pet model) throws SQLException {
        String sql = "INSERT INTO Pets (nome, especie, idDono, numeroConveniado, raca, dataNascimento) VALUES (?, ?, ?, ?, ?, ?);";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getEspecie());
        pst.setInt(3, model.getDono().getIdDono());
        pst.setString(4, model.getNumeroConveniado());
        pst.setString(5, model.getRaca());
        pst.setDate(6, java.sql.Date.valueOf(model.getDataNascimento()));
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean remove(Pet model) throws SQLException {
        String sql = "DELETE FROM Pets WHERE idPet = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean altera(Pet model) throws SQLException {
        String sql = "UPDATE Pets SET nome = ?, especie = ?, idDono = ?, numeroConveniado = ?, raca = ?, dataNascimento = ? WHERE idPet = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setString(1, model.getNome());
        pst.setString(2, model.getEspecie());
        pst.setInt(3, model.getDono().getIdDono());
        pst.setString(4, model.getNumeroConveniado());
        pst.setString(5, model.getRaca());
        pst.setDate(6, java.sql.Date.valueOf(model.getDataNascimento()));
        pst.setInt(7, model.getId());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public Pet buscaID(Pet model) throws SQLException {
        String sql = "SELECT * FROM Pets WHERE idPet = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getId());
        rs = pst.executeQuery();
        if (rs.next()) {
            DonoDAO donoDAO = new DonoDAO();
            Dono dono = new Dono();
            dono.setIdDono(rs.getInt("idDono"));
            dono = donoDAO.buscaID(dono);
            pet = new Pet(rs.getInt("idPet"), rs.getString("nome"), rs.getString("especie"), rs.getString("numeroConveniado"), rs.getString("raca"), rs.getDate("dataNascimento").toLocalDate(), dono);
        }
        Banco.desconectar();
        return pet;
    }

    @Override
    public Collection<Pet> lista(String criterio) throws SQLException {
        Collection<Pet> listagem = new ArrayList<>();
        String sql = "SELECT * FROM Pets";
        if (!criterio.isEmpty()) {
            sql += " WHERE " + criterio;
        }
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            DonoDAO donoDAO = new DonoDAO();
            Dono dono = new Dono(); 
            dono.setIdDono(rs.getInt("idDono"));
            dono = donoDAO.buscaID(dono);
            pet = new Pet(rs.getInt("idPet"), rs.getString("nome"), rs.getString("especie"), rs.getString("numeroConveniado"), rs.getString("raca"), rs.getDate("dataNascimento").toLocalDate(), dono);
            listagem.add(pet);
        }
        Banco.desconectar();
        return listagem;
    }
}
