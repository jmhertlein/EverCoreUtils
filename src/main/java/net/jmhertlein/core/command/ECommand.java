/*
 * Copyright (C) 2013 Joshua Michael Hertlein <jmhertlein@gmail.com>
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
package net.jmhertlein.core.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Joshua
 * @deprecated use the Extensible Bukkit Command Framework instead
 */
public class ECommand {

    public static final String DISABLE_AUTOACTIVE = "-na",
            RECURSIVE = "-r",
            ADMIN = "-admin",
            ALL = "-A",
            NO_AUTOBUILD_PLOT_SIGN = "-nosign",
            VERBOSE = "-v",
            VERY_VERBOSE = "-vv",
            VERY_VERY_VERBOSE = "-vvv";
    private List<String> args, flags;

    /**
     * Converts elements available in onCommand into an MCTCommand
     *
     * @param label the first word after the slash
     * @param args all following words and args
     */
    public ECommand(String label, String[] args) {
        this.args = new ArrayList<>();
        this.flags = new ArrayList<>();

        this.args.add(label);

        for (String s : args) {
            if (s.startsWith("-")) {
                this.flags.add(s);
            } else {
                this.args.add(s);
            }
        }
    }

    /**
     * Parses the given command to make an MCTCommand that represents it
     *
     * @param slashCommand
     */
    public ECommand(String slashCommand) {
        this.args = new ArrayList<>();
        this.flags = new ArrayList<>();

        slashCommand = slashCommand.substring(1); //take off the leading /

        Scanner scan = new Scanner(slashCommand);



        String s;
        while (scan.hasNext()) {
            s = scan.next();
            if (s.startsWith("-")) {
                flags.add(s);
            } else {
                args.add(s);
            }
        }
        scan.close();
    }

    /**
     * Copies the passed args and flags into a new MCTCommand
     *
     * @param args
     * @param flags
     */
    public ECommand(String[] args, String[] flags) {
        this.args = new ArrayList<>();
        this.flags = new ArrayList<>();

        this.args.addAll(Arrays.asList(args));
        this.flags.addAll(Arrays.asList(flags));
    }
    
    /**
     * Compatibility constructor to bridge between EBCF and old handler/ECommand system
     * @param nodes
     * @param arguments 
     */
    public ECommand(List<String> nodes, String[] arguments) {
        this.args = nodes;
        this.flags = new ArrayList<>();
        
        for(String s : arguments)
            if(s.startsWith("-"))
                flags.add(s);
            else
                args.add(s);
    }

    /**
     * Checks whether or not the flag is present in the command.
     *
     * @param flag
     * @return
     */
    public boolean hasFlag(String flag) {
        return flags.contains(flag);
    }

    @Override
    public String toString() {
        String nu = "/";
        for (String s : args) {
            nu += s;
            nu += " ";
        }

        nu += "(";
        for (String s : flags) {
            nu += s;
            nu += ", ";
        }
        nu += ")";

        return nu;
    }

    public String getArgAtIndex(final int i) throws ArgumentCountException {


        if (i >= args.size()) {
            throw new ArgumentCountException(i);
        }

        return args.get(i);
    }

    public boolean hasArgAtIndex(int i) {
        return args.size() > i;
    }

    public String get(int i) throws ArgumentCountException {
        return getArgAtIndex(i);
    }

    /**
     * Note: Specific to MCTowns applications Assumes that the command is trying
     * to flag a region (i.e. this.get(0).equals(some town level),
     * get(1).equals("flag"), get(3).equals(some flag name)) and turns arguments
     * at indices in the rage [3, end) into a string array and returns it.
     *
     * @return arguments for the specified flag
     */
    public String[] getFlagArguments() {
        String[] flagArgs = new String[args.size() - 3];

        for (int i = 3; i < args.size(); i++) {
            flagArgs[i - 3] = args.get(i);
        }

        return flagArgs;
    }

    /**
     * Concatenates and returns all the non-flag arguments with indices in the
     * range [index, END) where END is the index of the last argument.
     *
     * @param index index to begin concatenation at
     * @return the constructed String
     */
    public String concatAfter(int index) {
        if (index == args.size() - 1) {
            return args.get(index);
        }

        return args.get(index) + " " + concatAfter(index + 1);
    }
}
