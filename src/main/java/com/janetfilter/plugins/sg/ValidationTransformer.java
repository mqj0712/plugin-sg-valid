package com.janetfilter.plugins.sg;

import static jdk.internal.org.objectweb.asm.Opcodes.ASM5;

import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.plugin.MyTransformer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;


public class ValidationTransformer implements MyTransformer {

  private String className;

  private String methodName;
  private String methodDesc;

  @Override
  public String getHookClassName() {
    return this.className;
  }

  public ValidationTransformer(String className, String methodName, String methodDesc) {
    this.className = className;
    this.methodName = methodName;
    this.methodDesc = methodDesc;
  }

  @Override
  public byte[] transform(String className, byte[] classBytes, int order) throws Exception {
    ClassReader classReader = new ClassReader(classBytes);
    ClassNode classNode = new ClassNode(ASM5);
    classReader.accept(classNode, 0);
    for (MethodNode mn : classNode.methods) {
      if (methodName.equals(mn.name) && methodDesc.equals(mn.desc)) {
        DebugInfo.debug(String.format("method %s.%s (%s) matched", this.className, this.methodName,
            this.methodDesc));
        mn.instructions.clear();
        mn.instructions.insert(new InsnNode(Opcodes.RETURN));
        mn.visitEnd();
      }
    }
    ClassWriter writer = new ClassWriter(0);
    classNode.accept(writer);
    return writer.toByteArray();
  }

}
