package model;

import java.time.LocalDate;

public class Exame {
    private Long id;
    private String tipo;
    private LocalDate dataExame;
    private String resultado;
    private Long pacienteId;
    private Long medicoId;

    public Exame() {
    }

    public Exame(Long id, String tipo, LocalDate dataExame, String resultado, Long pacienteId) {
        this(id, tipo, dataExame, resultado, pacienteId, null);
    }

    public Exame(Long id, String tipo, LocalDate dataExame, String resultado, Long pacienteId, Long medicoId) {
        this.id = id;
        this.tipo = tipo;
        this.dataExame = dataExame;
        this.resultado = resultado;
        this.pacienteId = pacienteId;
        this.medicoId = medicoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataExame() {
        return dataExame;
    }

    public void setDataExame(LocalDate dataExame) {
        this.dataExame = dataExame;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public Long getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(Long medicoId) {
        this.medicoId = medicoId;
    }
}
