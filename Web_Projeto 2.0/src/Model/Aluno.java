package Model;

import DAO.AlunoDAO;

/**
*
* @author Grupo PI
*/
public class Aluno extends Usuario {

    private String ra;
    private Turma turma;
    private Grupo grupo;
        
    public Aluno(String email, String senha) {
        super(email, senha);
    }
    
    public Aluno(int id, String nome, String email, String senha, String ra) {
        super(id, nome, email, senha);
        this.ra = ra;
    }
    
    public Aluno() {
    	
    }

	public String getRa() {
        return this.ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	public void create() {
		AlunoDAO dao = new AlunoDAO();
		dao.create(this);
	}
	
	public void load() {
		AlunoDAO dao = new AlunoDAO();
		dao.load(this.getIdUsuario());
	}
	
	public void update() {
		AlunoDAO dao = new AlunoDAO();		
		dao.update(this);
	}
	
	@Override
	public String toString() {
		return "Aluno [id=" + this.getIdUsuario() + " ra=" + this.getRa() + " nome=" + this.getNome() + " email=" + this.getEmail() + "]"; 
	}
   
}