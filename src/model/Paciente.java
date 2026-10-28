package model;

public class Paciente {
    private Long id;
    private String nome;
    private String email;
    private String CPF;

    public Paciente  (Long id, String nome, String email, String CPF){
        this.id= id;
        setNome(nome);
        setEmail(email);
        setCPF(CPF);
        setId(id);

    }

    public Long getId() {
        return id;
    }

    public void setNome(String nome){
        this.nome= nome;
    }

    public String getNome(){
        return nome;
    }

    public String getCPF() {
        return CPF;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void setEmail(String email) {
    }

    private void setCPF(String cpf) {
    }
}
