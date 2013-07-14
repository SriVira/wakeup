package com.cletomcj.wakeup.retos;

/**
 * This is an auxiliary class wich associates a math operation with a
 * String value to represent on the AlarmActivity
 * @author Carlos Martin-Cleto
 *
 */
public class Operation {
	
	private int resultado;
	private String texto;
	
	public Operation(int resultado, String texto) {
		super();
		this.resultado = resultado;
		this.texto = texto;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	
}
