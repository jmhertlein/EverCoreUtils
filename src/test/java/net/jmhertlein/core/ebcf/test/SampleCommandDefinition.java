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
import net.jmhertlein.core.ebcf.annotation.CommandMethod;
import org.bukkit.command.CommandSender;

/**
 *
 * @author joshua
 */
public class SampleCommandDefinition implements CommandDefinition {
    private String ran;
    private int argsPassed;
    
    @CommandMethod(path = "say hi", console = true)
    public void sayHi() {
        System.out.println("Hi");
        ran = "sayhi";
    }
    
    @CommandMethod(path = "this one is really pretty long", console = true)
    public void longCmd(CommandSender s, String[] args) {
        ran = "longcmd";
    }
    
    @CommandMethod(path = "this one is really pretty different", console = true)
    public void longCmdDiff(CommandSender s, String[] args) {
        ran="longcmddiff";
    }
    
    @CommandMethod(path = "long command path", requiredArgs = 3, console = true)
    public void longCmdWithArgs(CommandSender s, String[] args) {
        System.out.println("I was called with args:");
        for(String arg : args) {
            System.out.println(arg);
        }
        ran = "longcmdwithargs";
        argsPassed = args.length;
    }

    public int getArgsPassed() {
        return argsPassed;
    }
    
    @CommandMethod(path = "say bye", console = true)
    public void sayBye(CommandSender s, String[] args) {
        System.out.println("Bye");
        ran="saybye";
    }
    
    @CommandMethod(path = "sayone", console = true)
    public void sayOne(CommandSender s, String[] args) {
        System.out.println("One");
        ran="sayone";
    }
    
    @CommandMethod(path = "echo", requiredArgs = 1, console = true, helpMsg = "This is help for echo. Usage: /echo <message>")
    public void echo(CommandSender sender, String[] args) {
        StringBuilder b = new StringBuilder();
        for(String s : args) {
            b.append(s);
            b.append(' ');
        }
        System.out.println(b.toString());
        ran="echo";
    }
    
    @CommandMethod(path = "param args", console = true)
    public void paramArgs(String[] args) {
        ran="paramargs";
    }
    
    @CommandMethod(path = "param sender", console = true)
    public void paramSender(CommandSender sender) {
        ran="paramsender";
    }

    public String getRan() {
        return ran;
    }

}