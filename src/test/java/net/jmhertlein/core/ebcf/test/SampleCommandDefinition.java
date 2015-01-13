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

import net.jmhertlein.core.ebcf.CommandDefinition;
import net.jmhertlein.core.ebcf.annotation.ExecutableCommand;
import org.bukkit.command.CommandSender;

/**
 *
 * @author joshua
 */
public class SampleCommandDefinition implements CommandDefinition {
    private String ran;
    
    @ExecutableCommand(path = "say hi", console = true)
    public void sayHi(CommandSender s, String[] args) {
        System.out.println("Hi");
        ran = "sayhi";
    }
    
    @ExecutableCommand(path = "this one is really pretty long", console = true)
    public void longCmd(CommandSender s, String[] args) {
        ran = "longcmd";
    }
    
    @ExecutableCommand(path = "this one is really pretty different", console = true)
    public void longCmdDiff(CommandSender s, String[] args) {
        ran="longcmddiff";
    }
    
    @ExecutableCommand(path = "say bye", console = true)
    public void sayBye(CommandSender s, String[] args) {
        System.out.println("Bye");
        ran="saybye";
    }
    
    @ExecutableCommand(path = "sayone", console = true)
    public void sayOne(CommandSender s, String[] args) {
        System.out.println("One");
        ran="sayone";
    }
    
    @ExecutableCommand(path = "echo", requiredArgs = 1, console = true, helpMsg = "This is help for echo. Usage: /echo <message>")
    public void echo(CommandSender sender, String[] args) {
        StringBuilder b = new StringBuilder();
        for(String s : args) {
            b.append(s);
            b.append(' ');
        }
        System.out.println(b.toString());
        ran="echo";
    }

    public String getRan() {
        return ran;
    }

}
