/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author andre
 */
public enum Priority {
	HIGH("Alta"), 
	MEDIUM("Média"), 
	LOW("Baixa");

	private String label;

	private Priority(String label) {
	        this.label = label;
	    }

	public String getLabel() {
		return label;
	}
}
