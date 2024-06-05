package br.com.fatec.DAO;

import br.com.fatec.model.Agendamento;
import br.com.fatec.model.Dono;
import br.com.fatec.model.Pet;
import br.com.fatec.model.Unidade;
import br.com.fatec.model.Veterinario;
import br.com.fatec.persistencia.Banco;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class AgendamentoDAO implements DAO<Agendamento> {

    private Agendamento agendamento;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public boolean insere(Agendamento model) throws SQLException {
        String sql = "INSERT INTO Agendamentos (data, horario, idPet, idDono, idUnidade, idVeterinario, especialidade) VALUES (?, ?, ?, ?, ?, ?, ?);";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setDate(1, java.sql.Date.valueOf(model.getData()));
        pst.setTime(2, java.sql.Time.valueOf(model.getHorario()));
        pst.setInt(3, model.getPet().getId());
        pst.setInt(4, model.getDono().getIdDono());
        pst.setInt(5, model.getUnidade().getIdUnidade());
        pst.setInt(6, model.getVeterinario().getIdVeterinario());
        pst.setString(7, model.getEspecialidade());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean remove(Agendamento model) throws SQLException {
        String sql = "DELETE FROM Agendamentos WHERE idAgendamento = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdAgendamento());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public boolean altera(Agendamento model) throws SQLException {
        String sql = "UPDATE Agendamentos SET data = ?, horario = ?, idPet = ?, idDono = ?, idUnidade = ?, idVeterinario = ?, especialidade = ? WHERE idAgendamento = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setDate(1, java.sql.Date.valueOf(model.getData()));
        pst.setTime(2, java.sql.Time.valueOf(model.getHorario()));
        pst.setInt(3, model.getPet().getId());
        pst.setInt(4, model.getDono().getIdDono());
        pst.setInt(5, model.getUnidade().getIdUnidade());
        pst.setInt(6, model.getVeterinario().getIdVeterinario());
        pst.setString(7, model.getEspecialidade());
        pst.setInt(8, model.getIdAgendamento());
        boolean result = pst.executeUpdate() >= 1;
        Banco.desconectar();
        return result;
    }

    @Override
    public Agendamento buscaID(Agendamento model) throws SQLException {
        String sql = "SELECT * FROM Agendamentos WHERE idAgendamento = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setInt(1, model.getIdAgendamento());
        rs = pst.executeQuery();
        if (rs.next()) {
            // Criando o objeto de DAO e dos models para popular os objetos pelo ID cadastrado no banco de dados.
            DonoDAO donoDAO = new DonoDAO();
            Dono dono = new Dono();
            dono.setIdDono(rs.getInt("idDono"));
            dono = donoDAO.buscaID(dono);
            
            PetDAO petDAO = new PetDAO();
            Pet pet = new Pet(dono);
            pet.setId(rs.getInt("idPet"));
            pet = petDAO.buscaID(pet);
            
            
            UnidadeDAO unidadeDAO = new UnidadeDAO();
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(rs.getInt("idUnidade"));
            unidade = unidadeDAO.buscaID(unidade);
            
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            Veterinario veterinario = new Veterinario(unidade);
            veterinario.setIdVeterinario(rs.getInt("idVeterinario"));
            veterinario = veterinarioDAO.buscaID(veterinario);
            
            agendamento = new Agendamento(rs.getInt("idAgendamento"), dono, pet, veterinario, unidade, rs.getString("especialidade"), rs.getDate("data").toLocalDate(), rs.getTime("horario").toLocalTime());
        }
        Banco.desconectar();
        return agendamento;
    }

    @Override
    public Collection<Agendamento> lista(String criterio) throws SQLException {
        Collection<Agendamento> listagem = new ArrayList<>();
        String sql = "SELECT * FROM Agendamentos";
        if (!criterio.isEmpty()) {
            sql += " WHERE " + criterio;
        }
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            // Criando o objeto de DAO e dos models para popular os objetos pelo ID cadastrado no banco de dados.
            DonoDAO donoDAO = new DonoDAO();
            Dono dono = new Dono();
            dono.setIdDono(rs.getInt("idDono"));
            dono = donoDAO.buscaID(dono);
            
            PetDAO petDAO = new PetDAO();
            Pet pet = new Pet(dono);
            pet.setId(rs.getInt("idPet"));
            pet = petDAO.buscaID(pet);
            
            
            UnidadeDAO unidadeDAO = new UnidadeDAO();
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(rs.getInt("idUnidade"));
            unidade = unidadeDAO.buscaID(unidade);
            
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            Veterinario veterinario = new Veterinario(unidade);
            veterinario.setIdVeterinario(rs.getInt("idVeterinario"));
            veterinario = veterinarioDAO.buscaID(veterinario);
            
            agendamento = new Agendamento(rs.getInt("idAgendamento"), dono, pet, veterinario, unidade, rs.getString("especialidade"), rs.getDate("data").toLocalDate(), rs.getTime("horario").toLocalTime());
            listagem.add(agendamento);
        }
        Banco.desconectar();
        return listagem;
    }

    public Agendamento buscaPorDataHorario(LocalDate data, LocalTime horario) throws SQLException {
        String sql = "SELECT * FROM Agendamentos WHERE data = ? AND horario = ?;";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        pst.setDate(1, java.sql.Date.valueOf(data));
        pst.setTime(2, java.sql.Time.valueOf(horario));
        rs = pst.executeQuery();
        Agendamento agendamento = null;
        if (rs.next()) {
            // Criando o objeto de DAO e dos models para popular os objetos pelo ID cadastrado no banco de dados.
            DonoDAO donoDAO = new DonoDAO();
            Dono dono = new Dono();
            dono.setIdDono(rs.getInt("idDono"));
            dono = donoDAO.buscaID(dono);

            PetDAO petDAO = new PetDAO();
            Pet pet = new Pet(dono);
            pet.setId(rs.getInt("idPet"));
            pet = petDAO.buscaID(pet);


            UnidadeDAO unidadeDAO = new UnidadeDAO();
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(rs.getInt("idUnidade"));
            unidade = unidadeDAO.buscaID(unidade);

            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            Veterinario veterinario = new Veterinario(unidade);
            veterinario.setIdVeterinario(rs.getInt("idVeterinario"));
            veterinario = veterinarioDAO.buscaID(veterinario);

            agendamento = new Agendamento(rs.getInt("idAgendamento"), dono, pet, veterinario, unidade, rs.getString("especialidade"), rs.getDate("data").toLocalDate(), rs.getTime("horario").toLocalTime());
        }
        Banco.desconectar();
        return agendamento;
    }

    public Set<LocalDate> getFullDays() throws SQLException {
        Set<LocalDate> fullDays = new HashSet<>();
        String sql = "SELECT data FROM agendamentos GROUP BY data HAVING COUNT(*) = 7";
        Banco.conectar();
        pst = Banco.getConexao().prepareStatement(sql);
        rs = pst.executeQuery();
        while (rs.next()) {
            LocalDate date = rs.getDate("data").toLocalDate();
            fullDays.add(date);
        }
        return fullDays;
    }
}
