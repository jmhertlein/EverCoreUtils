/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.jmhertlein.core.io;

import java.io.File;

/**
 *
 * @author joshua
 */
public class Files {
    public static final File join(String ... path) {
        File cur = new File(path[0]);
        
        for(int i = 1; i < path.length; i++ ) {
            cur = new File(cur, path[i]);
        }
        
        return cur;
    }
}
