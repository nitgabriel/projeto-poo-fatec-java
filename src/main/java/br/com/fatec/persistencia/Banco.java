package br.com.fatec.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banco {
   
    //variaveis internas
    private static String usuario = "root";
    private static String senha = "admin";
    private static String servidor = "localhost";
    private static String bd = "Ultrapets";
    private static int porta = 3306;
    
    //variaveis de banco
    private static Connection conexao = null;
    
    public static void conectar() 
            throws SQLException {
        String url="jdbc:mysql://" + servidor +
                   ":" + porta +
                   "/" + bd;
        
        //Conectar ao banco
        conexao = DriverManager.getConnection(url, usuario, senha);
    }
    
    //Devolve a conexão criada para o usuario
    public static Connection getConexao() {
        return conexao;
    }
    
    public static void desconectar() 
            throws SQLException {
        conexao.close();
    }
}
