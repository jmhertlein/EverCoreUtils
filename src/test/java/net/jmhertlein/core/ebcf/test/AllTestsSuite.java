/*
 * Copyright (C) 2015 Joshua Michael Hertlein <jmhertlein@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
