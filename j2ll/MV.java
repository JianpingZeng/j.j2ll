package j2ll;

import org.objectweb.asm.*;

import java.io.PrintStream;
import java.util.*;

/**
 */
public class MV extends MethodVisitor{

    // parent
    private CV cv;

    // state
    String methodName;
    String javaSignature;

    // arguments
    List<String> _argTypes;
    // res
    String _resType;

    // local vars
    Map<Integer, _LocalVar> vars = new HashMap<Integer, _LocalVar>();
    // buffer
    List<String> out = new ArrayList<String>();
    // stack
    _Stack stack = new _Stack();
    // labels
    List<Label> labels = new ArrayList<Label>();
    List<String> usedLabels = new ArrayList<String>();

    int max_local;
    int max_stack;
    int tmp = 0;

    public MV(int i, String methodName, String javaSignature, CV cv) {
        super(i);
        this.methodName = methodName;
        this.javaSignature = javaSignature;
        _JavaSignature s = new _JavaSignature(this.javaSignature);
        _argTypes = s.getArgs();
        _resType = s.getResult();
        this.cv = cv;
    }

    public MV(int i, MethodVisitor methodVisitor) {
        super(i, methodVisitor);
    }

    @Override
    public void visitParameter(String s, int i) {
        System.out.println("VP " + s + " " + i);
    }

    @Override
    public AnnotationVisitor visitAnnotationDefault() {
        return super.visitAnnotationDefault();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String s, boolean b) {
        return super.visitAnnotation(s, b);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTypeAnnotation(i, typePath, s, b);
    }

    @Override
    public AnnotationVisitor visitParameterAnnotation(int i, String s, boolean b) {
        return super.visitParameterAnnotation(i, s, b);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        System.out.println("VA " + attribute);
    }

    @Override
    public void visitCode() {
    }

    @Override
    public void visitFrame(int i, int i1, Object[] objects, int i2, Object[] objects1) {
        out.add("; TODO FRame: " + i + " " + i1 + " " + i2);
    }

    @Override
    public void visitInsn(int opcode) {
        // todo 0 + 1 (nop + null)
        if (opcode >= Opcodes.ICONST_M1 && opcode <= Opcodes.ICONST_5) { // [2..8]
            int value = opcode - Opcodes.ICONST_0;
            stack.push(value);
            out.add("; push " + value);
        } else if (opcode >= Opcodes.LCONST_0 && opcode <= Opcodes.LCONST_1) { // [9..10]
            int value = opcode - Opcodes.LCONST_0;
            stack.push(value);
            out.add("; push " + value);
        } else if (opcode == Opcodes.IALOAD) { // 46
            String opindex = stack.pop(); String opref = stack.pop(); String value = stack.push();
            out.add("%__tmp" + tmp + " = getelementptr [0 x i32]* " + opref + ", i32 0, i32 " + opindex); // todo type index
            out.add(value + " = load i32* %__tmp" + tmp);
            tmp++;
        } else if (opcode == Opcodes.IASTORE) { // 79 todo
            String value = stack.pop(); String opindex = stack.pop(); String opref = stack.pop();
            out.add("%__tmp" + tmp + " = getelementptr [0 x i32]* " + opref + ", i32 0, i32 " + opindex); // todo type index
            out.add("store i32 " + value + ", i32* %__tmp" + tmp);
            tmp++;
        } else if (opcode == Opcodes.IADD) { // 96
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = add i32 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.LADD) { // 97
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = add i64 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.FADD) { // 98
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fadd float " + op1 + ", " + op2);
        } else if (opcode == Opcodes.DADD) { // 99
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fadd double " + op1 + ", " + op2);
        } else if (opcode == Opcodes.ISUB) { // 100
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = sub i32 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.LSUB) { // 101
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = sub i64 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.FSUB) { // 102
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fsub float " + op1 + ", " + op2);
        } else if (opcode == Opcodes.DSUB) { // 103
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fsub double " + op1 + ", " + op2);
        } else if (opcode == Opcodes.IMUL) { // 104
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = mul i32 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.LMUL) { // 105
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = mul i64 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.FMUL) { // 106
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fmul float " + op1 + ", " + op2);
        } else if (opcode == Opcodes.DMUL) { // 107
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fmul double " + op1 + ", " + op2);
        } else if (opcode == Opcodes.IDIV) { // 108
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = sdiv i32 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.LDIV) { // 109
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = sdiv i64 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.FDIV) { // 110
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fdiv float " + op1 + ", " + op2);
        } else if (opcode == Opcodes.DDIV) { // 111
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = fdiv double " + op1 + ", " + op2);
        } else if (opcode == Opcodes.IREM) { // 112
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = srem i32 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.LREM) { // 113
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = srem i64 " + op1 + ", " + op2);
        } else if (opcode == Opcodes.FREM) { // 114
            String op2 = stack.pop(); String op1 = stack.pop(); String op3 = stack.push();
            out.add(op3 + " = frem float " + op1 + ", " + op2);
        } else if (opcode == Opcodes.I2S) { // 147
            String op = stack.pop();
            out.add("i2s " + op);
        } else if (opcode == Opcodes.IRETURN) { // 172
            String op = stack.pop();
            out.add("ret i32 " + op);
        } else if (opcode == Opcodes.LRETURN) { // 173
            String op = stack.pop();
            out.add("ret i64 " + op);
        } else if (opcode == Opcodes.FRETURN) { // 174
            String op = stack.pop();
            out.add("ret float " + op);
        } else if (opcode == Opcodes.DRETURN) { // 175
            String op = stack.pop();
            out.add("ret double " + op);
        } else if (opcode == Opcodes.RETURN) { // 177
            out.add("ret void");
        } else {
            System.out.println("IN " + opcode);
        }
    }

    @Override
    public void visitIntInsn(int opcode, int value) {
        if (opcode == Opcodes.BIPUSH) {  // 16
            stack.push(value);
            out.add("; push " + value);
        } else if (opcode == Opcodes.SIPUSH) { // 17
            stack.push(value);
            out.add("; push " + value);
        } else if (opcode == Opcodes.NEWARRAY) { // 188
            String op = stack.pop();
            String res = stack.push();
            out.add("%__tmp" + tmp + " = call i8* @malloc(i32 " + op + ")");
            out.add(res + " = bitcast i8* %__tmp" + tmp + " to [0 x i32]*");
            tmp++;
        } else {
            System.out.println("INI " + opcode + " " + value);
        }
    }

    @Override
    public void visitVarInsn(int opcode, int slot) {
        if (opcode == Opcodes.ILOAD) { // 21
            // copy from local to stack
            String op = stack.push();
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.LLOAD) { // 22
            // copy from local to stack TODO
            String op = stack.push();
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.FLOAD) { // 23
            // copy from local to stack TODO
            String op = stack.push();
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.DLOAD) { // 24
            // copy from local to stack TODO
            String op = stack.push();
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.ALOAD) { // 25
            // copy from local to stack TODO
            String op = stack.push();
            out.add(op + " = load slot-pointer:" + slot);
        } else if (opcode == Opcodes.ISTORE) { // 54
            // copy from stack to local
            String op = stack.pop();
            // demo store i32 %tmp, i32* %x.ptr
            out.add("store i32 " + op + ", slot-pointer:" + slot);
        } else if (opcode == Opcodes.LSTORE) { // 55 todo
            // copy from stack to local
            String op = stack.pop();
            // demo store i32 %tmp, i32* %x.ptr
            out.add("store i64 " + op + ", slot-pointer:" + slot);
        } else if (opcode == Opcodes.ASTORE) { // 58 todo
            // copy from stack to local
            String op = stack.pop();
            out.add("store [0 x i32]* " + op + ", slot-pointer:" + slot);
        } else {
            System.out.println("VVI " + opcode + " " + slot);
        }
    }

    @Override
    public void visitTypeInsn(int i, String s) {
        System.out.println("VTI " + i + " " + s);
    }

    @Override
    public void visitFieldInsn(int opcode, String s, String s1, String s2) {
        if (opcode == Opcodes.PUTSTATIC) { // 179 todo
            out.add("putstatic " + s + " " + s1 + " " + s2);
        } else if (opcode == Opcodes.PUTFIELD) { // 181 todo
            out.add("putfield " + s + " " + s1 + " " + s2);
        } else {
            System.out.println("VFI " + opcode + " " + s);
        }
    }

    @Override
    public void visitMethodInsn(int i, String s, String s1, String s2) {
        System.out.println("VMI " + i + " " + s);
    }

    @Override
    public void visitMethodInsn(int opcode, String className, String methodName, String signature, boolean b) {
        if (opcode == Opcodes.INVOKESPECIAL) { // 183 todo
            _JavaSignature s = new _JavaSignature(signature);
            String call = s.getSignatureCall(className, methodName, this.stack);
            if ("void".equals(s.getResult())) {
                out.add("call " + call);
            } else {
                String op = stack.push();
                out.add(op + " = call " + call);
            }

            // declare
            if (!this.cv.className.equals(className)) {
                this.cv.declares.add(s.getSignatureDeclare(className, methodName));
            }

        } else if (opcode == Opcodes.INVOKESTATIC) { // 184
            _JavaSignature s = new _JavaSignature(signature);
            String call = s.getSignatureCall(className, methodName, this.stack);
            if ("void".equals(s.getResult())) {
                out.add("call " + call);
            } else {
                String op = stack.push();
                out.add(op + " = call " + call);
            }

            // declare
            if (!this.cv.className.equals(className)) {
                this.cv.declares.add(s.getSignatureDeclare(className, methodName));
            }

        } else {
            System.out.println("VMI " + opcode);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String s, String s1, Handle handle, Object... objects) {
        System.out.println("VIDI " + s + " " + s1);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        if (opcode == Opcodes.GOTO) {
            usedLabels.add(label.toString());
            out.add("br label %" + label);
        } else if (opcode == Opcodes.IF_ICMPGE) {
            usedLabels.add(label.toString());
            //  %cond = icmp eq i32 %a, %b
            // br i1 %cond, label %IfEqual, label %IfUnequal
            // IfEqual:
            String op2 = stack.pop();
            String op1 = stack.pop();
            out.add("%__tmp" + tmp + " = icmp sge i32 " + op1 + ", " + op2);
            out.add("br i1 %__tmp" + tmp + ", label %" + label + ", label %__tmpl" + tmp);
            out.add("__tmpl" + tmp + ":");
            tmp++;
        } else {
            out.add("GOTO " + opcode + " " + label.toString());
        }
    }

    @Override
    public void visitLabel(Label label) {
        labels.add(label);
        out.add("label:" + label.toString());
    }

    @Override
    public void visitLdcInsn(Object o) {
        if (o instanceof String) {
            // const
            stack.push(10);
            out.add("VLDC " + o);
        } else if (o instanceof Long) {
            Long value = (Long) o;
            stack.push(value.longValue());
            out.add("; push " + value);
        }
    }

    @Override
    public void visitIincInsn(int slot, int value) {
        out.add("inc:" + slot + ":" + value);
    }

    @Override
    public void visitTableSwitchInsn(int i, int i1, Label label, Label... labels) {
        super.visitTableSwitchInsn(i, i1, label, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label label, int[] ints, Label[] labels) {
        super.visitLookupSwitchInsn(label, ints, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String s, int i) {
        super.visitMultiANewArrayInsn(s, i);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitInsnAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s) {
        super.visitTryCatchBlock(label, label1, label2, s);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int i, TypePath typePath, String s, boolean b) {
        return super.visitTryCatchAnnotation(i, typePath, s, b);
    }

    @Override
    public void visitLocalVariable(String name, String sign, String s2, Label label, Label label1, int slot) {
        //System.out.println("VLV + " + name + " / " + sign + " " + s2 + " " + slot);
        vars.put(slot, new _LocalVar(name, sign));
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int i, TypePath typePath, Label[] labels, Label[] labels1, int[] ints, String s, boolean b) {
        return super.visitLocalVariableAnnotation(i, typePath, labels, labels1, ints, s, b);
    }

    @Override
    public void visitLineNumber(int i, Label label) {
        //System.out.println("LN " + i + " " + label);
        //super.visitLineNumber(i, label);
    }

    public void visitMaxs(int stack, int local) {
        // Maximums
        this.max_stack = stack;
        this.max_local = local;
    }

    @Override
    public void visitEnd() {
    }


    public void out(PrintStream ps) {
        // 0) info
        _JavaSignature ss = new _JavaSignature(this.javaSignature);

        ps.println("define " + this._resType +  " @" + Util.classMethodSignature2id(this.cv.className, this.methodName, ss) + "(" + Util.enumArgs(this._argTypes, "%s") + ") {");

        ps.print("    ; locals: ");
        ps.println(max_local);
        ps.print("    ; stack: ");
        ps.println(max_stack);
        ps.print("    ; args: ");
        ps.println(this._argTypes.size());

        // 1) local vars & args
        int cntSlot = 0;
        for (Integer i : vars.keySet()) {
            _LocalVar lv = vars.get(i);
            cntSlot++;
            // local var
            ps.print("    ");
            ps.println("%" + lv.name + " = alloca " + Util.javaSignature2irType(lv.signature) +  "\t\t; slot " + i + " = " + lv.signature);
            // init from arg (!)
            if (this._argTypes.size() < cntSlot) continue;
            String argType = this._argTypes.get(cntSlot-1);
            ps.println("    store "+argType+" %s" + i + ", "+argType+"* %" + lv.name); //todo
        }
        // 2) text
        for (String str : out) {
            int p = str.indexOf("slot-pointer");
            if (p != -1) {
                for (Integer slot : vars.keySet()) {
                    _LocalVar lv = vars.get(slot);
                    String s = "slot-pointer:" + slot;
                    String r = Util.javaSignature2irType(lv.signature) + "* %" + lv.name; // todo
                    str = str.replace(s, r);
                }
            }
            // label
            p = str.indexOf("label:");
            if (p != -1) {
                str = str.substring(6);
                if (usedLabels.contains(str)) {
                    ps.print("br label %");
                    ps.println(str);
                    str = str + ":";
                } else {
                    continue;
                }
            }
            // inc
            p = str.indexOf("inc:");
            if (p != -1) {
                str = str.substring(4);
                p = str.indexOf(":");
                int slot = Integer.parseInt(str.substring(0, p));
                int value = Integer.parseInt(str.substring(p + 1));
                _LocalVar var = this.vars.get(slot);
                if (var != null) {
                    //%x = load i32* %x.ptr        ; загрузили значение типа i32 по указателю %x.ptr
                    //%tmp = add i32 %x, 5         ; прибавили 5
                    //store i32 %tmp, i32* %x.ptr
                    ps.println("    %__tmp" + tmp + " = load i32* %" + var.name); tmp++;
                    ps.println("    %__tmp" + tmp + " = add i32 %__tmp" + (tmp - 1) + ", " + value); tmp++;
                    ps.println("    store i32 %__tmp" + (tmp - 1) + ", i32* %" + var.name);
                    continue;
                }
            }
            ps.print("    ");
            ps.println(str);
        }
        // 3) end
        ps.println("}");
        ps.println("");
    }
}