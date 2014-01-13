/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jmhertlein.core.ebcf.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author joshua
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    net.jmhertlein.core.ebcf.test.ManyCommandExecutionTest.class, 
    net.jmhertlein.core.ebcf.test.CommandLeafInsertionTest.class, 
    net.jmhertlein.core.ebcf.test.CommandExecutionTest.class, 
    net.jmhertlein.core.ebcf.test.CommandPassedCorrectArgumentsTest.class,
    net.jmhertlein.core.ebcf.test.CommandWithArgumentExecutionTest.class,
    net.jmhertlein.core.ebcf.test.InvalidCommandTest.class,
    net.jmhertlein.core.ebcf.test.NotEnoughRequiredArgsTest.class,
    net.jmhertlein.core.ebcf.test.AutoCompleteTest.class
})
public class AllTestsSuite {
    
}
