package j2ll;

import java.util.HashSet;
import java.util.Set;

/**
 * External class resolver
 */
public final class Resolver {

    private Set<String> classes = new HashSet<String>();

    public String resolve(String str) {
        if (classes.add(str))
            System.out.println("resolve " + str);
        return "%\"struct." + str + "\"*"; // todo syntax of struct -> internals
    }

    public String resolveStruct(String str) {
        if (classes.add(str))
            System.out.println("resolve " + str);
        return "%\"struct." + str + "\""; // todo syntax of struct -> internals
    }

    public Set<String> getClasses() {
        return classes;
    }
}
