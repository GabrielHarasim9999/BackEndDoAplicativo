package model;

public class Paciente {
    private Long id;
    private String nome;
    private String email;
    private String CPF;

    public Paciente() {
    }

    public Paciente(Long id, String nome, String email, String CPF) {
        setNome(nome);
        setEmail(email);
        setCPF(CPF);
        setId(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
