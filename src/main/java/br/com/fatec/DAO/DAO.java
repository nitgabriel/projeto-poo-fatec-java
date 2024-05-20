package br.com.fatec.DAO;

import java.sql.SQLException;
import java.util.List;


public interface DAO<T> {
    public boolean inserir(T obj)
            throws ClassNotFoundException, SQLException;
    
    public boolean alterar(T obj)
            throws ClassNotFoundException, SQLException;
    
    public T buscar(T obj)
            throws ClassNotFoundException, SQLException;
    
    public List<T> listar(String where)
            throws ClassNotFoundException, SQLException;
    
    public boolean remover(T obj)
            throws ClassNotFoundException, SQLException;
}
