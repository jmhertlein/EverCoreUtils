/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.ebcf.test;

import net.jmhertlein.core.ebcf.CommandDefinition;
import net.jmhertlein.core.ebcf.annotation.CommandMethod;

/**
 *
 * @author joshua
 */
public class SampleInvalidCommandDefinition implements CommandDefinition {
    private String ran;
    
    @CommandMethod(path = "invalid", console = true)
    public void invalidParams(Integer i) {
        ran = "invalidparams";
    }

    public String getRan() {
        return ran;
    }
    
}
