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

public class ValidationManagerTransformer implements MyTransformer {

  private final String classNamePattern;
  private final String methodNamePattern;
  private final String methodDescPattern;

  public ValidationManagerTransformer(String classNamePattern, String methodNamePattern,
      String methodDescPattern) {
    this.classNamePattern = classNamePattern;
    this.methodNamePattern = methodNamePattern;
    this.methodDescPattern = methodDescPattern;
  }

  @Override
  public String getHookClassName() {
//    we need to check uncertain classes, so return null here
    return null;
  }

  @Override
  public boolean isManager() {
//    to avoid conflict with certain classname rule, use manager here
    return true;
  }

  @Override
  public byte[] preTransform(String className, byte[] classBytes, int order) throws Exception {
    if(className.matches(this.classNamePattern)){
      ClassReader classReader = new ClassReader(classBytes);
      ClassNode classNode = new ClassNode(ASM5);
      classReader.accept(classNode, 0);
      boolean matched = false;
      for (MethodNode mn : classNode.methods) {
        if (mn.name.matches(this.methodNamePattern) && mn.desc.matches(this.methodDescPattern)) {
          DebugInfo.debug(String.format("method %s.%s (%s) matched", className, mn.name, mn.desc));
          mn.instructions.clear();
          mn.instructions.insert(new InsnNode(Opcodes.RETURN));
          mn.visitEnd();
          matched = true;
        }
      }
      if (matched) {
        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
      }
    }
    return classBytes;
  }
}
