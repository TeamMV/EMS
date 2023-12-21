package dev.mv.ems.runtime;

import dev.mv.ems.parser.ast.Statement;
import dev.mv.ems.parser.ast.Type;

import java.util.HashMap;
import java.util.Map;

public class Runtime {
    public Program program;

    public Runtime(Program program) {
       this.program = program;
    }

    public Map<String, double[]> run(int iterations, boolean includeStartVal) {
        Map<String, Variable> variables = new HashMap<>();
        Map<String, double[]> output = new HashMap<>();

        program.vars.forEach((k, v) -> {
            variables.put(k, v.clone());
            double[] arr = new double[iterations + (includeStartVal ? 1 : 0)];
            if (includeStartVal) arr[0] = v.getValueAsD();
            output.put(k, arr);
        });

        double[] arr = new double[iterations + (includeStartVal ? 1 : 0)];
        if (includeStartVal) arr[0] = 0;
        output.put("index", arr);

        for (int i = 1; i <= iterations; i++) {
            variables.put("index", new Variable("index", Type.INT).setValue(i));
            Visitor visitor = new Visitor(variables);
            for (Statement stmt : program.stmts) {
                visitor.visitStatement(stmt);
            }
            int fi = i - (includeStartVal ? 0 : 1);
            variables.forEach((k, v) -> {
                if (output.containsKey(k)) output.get(k)[fi] = v.getValueAsD();
            });
        }

        return output;
    }

}
