package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ConnectionFactory.Conexao;
import Model.Aluno;
import Model.Usuario;

public class AlunoDAO extends UsuarioDAO {
	
	/**
     * CRUD: Insere aluno
     * @param conn: Connection
	 * @throws SQLException 
     */
	public void create(Aluno aluno){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Usuario usuario = criar(aluno);
		
		String sqlComand = "INSERT INTO Aluno (aluno_id, ra) VALUES (?, ?)";
		
		try(PreparedStatement stm = conn.prepareStatement(sqlComand);){
			System.out.println(usuario.getIdUsuario());
			stm.setInt(1, usuario.getIdUsuario());
			stm.setString(2, aluno.getRa());
			
			stm.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
     * CRUD: Carrega dados do aluno
     * @param conn: Connection
	 * @throws SQLException 
     */
	public Aluno load(int id){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sqlComand = "SELECT aluno.ra, usuario.id, usuario.nome, usuario.email, usuario.senha " + 
		           			"FROM Aluno as aluno " + 
		           			"INNER JOIN Usuario as usuario ON usuario.Id = aluno.aluno_id " + 
		           			"WHERE usuario.id = ?";
		
		Aluno aluno = null;
		try(PreparedStatement stm = conn.prepareStatement(sqlComand);){
			
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			
            if(rs.next()) {
            	String email = rs.getString("usuario.email");
            	String senha = rs.getString("usuario.senha");
            	
            	aluno = new Aluno(email, senha);
            	
            	aluno.setIdUsuario(rs.getInt("usuario.id"));
            	aluno.setNome(rs.getString("usuario.nome"));
            	aluno.setRa(rs.getString("aluno.ra"));
            }
            
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return aluno;
	}
	
	/**
     * CRUD: Atualiza dados do aluno
     * @param conn: Connection
	 * @throws SQLException 
     */
	public void update(Aluno aluno){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Atualiza email e senha
		atualizar(aluno);
		
		String sqlComand = "UPDATE Aluno SET ra = ? WHERE aluno_id = ?";
		
		try(PreparedStatement stm = conn.prepareStatement(sqlComand);){
			stm.setString(1, aluno.getRa());
			stm.setInt(2, aluno.getIdUsuario());
			
			stm.executeUpdate();            
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * CRUD: Deleta usuÃ¡rio
     * @param conn: Connection
	 * @throws SQLException 
     */
	public void delete(int id){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sqlComand = "DELETE FROM Aluno WHERE aluno_id = ?";
		try(PreparedStatement stm = conn.prepareStatement(sqlComand);){
			stm.setInt(1, id);
			stm.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		excluir(id);
	}
	
	//passando um grupo id ele retorna a lista de alunos que pertence ao mesmo
	public ArrayList<Aluno> grupoAlunos(int id){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<Aluno> lista = new ArrayList<>();
		Aluno aluno = null;
		
		String sqlComand = 	"SELECT u.id, u.nome, u.email, u.senha, a.ra FROM grupo g "
								+ "JOIN turma_aluno t ON g.id = t.grupo_id "
								+ "JOIN aluno a ON t.aluno_id = a.aluno_id "
								+ "JOIN usuario u ON a.aluno_id = u.id WHERE g.id = ?";
				
		try(PreparedStatement stm = conn.prepareStatement(sqlComand)){
			
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			
			while(rs.next()) {
				int idAluno = rs.getInt("id");
				String nome = rs.getString("nome");
				String email = rs.getString("email");
				String senha = rs.getString("senha");
				String ra = rs.getString("ra");
				aluno = new Aluno(idAluno, nome, email, senha, ra);
				lista.add(aluno);
			} 
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	//retorna o id de turma_aluno de um aluno passando seu grupo e id do aluno
	public ArrayList<Integer> turmaAluno(int idGrupo, ArrayList<Aluno> listaAluno){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<Integer> lista = new ArrayList<Integer>();
		
		String sqlComand = 	"SELECT t.id FROM grupo g "
							+ "JOIN turma_aluno t ON g.id = t.grupo_id "
							+ "JOIN aluno a ON t.aluno_id = a.aluno_id "
							+ "WHERE g.id = ? and a.aluno_id = ?";
				
		try(PreparedStatement stm = conn.prepareStatement(sqlComand)){
			
			for(int i = 0; i < listaAluno.size(); i++) {
				stm.setInt(1, idGrupo);
				stm.setInt(2, listaAluno.get(i).getIdUsuario());
				ResultSet rs = stm.executeQuery();
				
				if(rs.next()) {
					int id = rs.getInt("id");
					lista.add(id);
				} 
			}
			
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	
	/**
     * CRUD: Carrega dados do aluno
     * @param conn: Connection
	 * @throws SQLException 
     */
	public Aluno loadTurmaAluno(int id){
		Connection conn = null;
		try {
			conn = new Conexao().obtemConexao();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sqlComand = "SELECT * FROM usuario u "
				+ "JOIN aluno a ON u.id = a.aluno_id "
				+ "JOIN turma_aluno t ON a.aluno_id = t.aluno_id "
				+ "WHERE t.id = ?";
		
		Aluno aluno = null;
		try(PreparedStatement stm = conn.prepareStatement(sqlComand);){
			
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			
            if(rs.next()) {
            	String email = rs.getString("email");
            	String senha = rs.getString("senha");
            	int idAluno = rs.getInt("id");
            	String nome = rs.getString("nome");
            	String ra = rs.getString("ra");
            	
            	aluno = new Aluno(idAluno, nome, email, senha, ra);
            }
            
		}catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return aluno;
	}
}