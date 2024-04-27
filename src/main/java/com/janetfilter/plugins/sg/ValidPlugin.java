package com.janetfilter.plugins.sg;

import com.janetfilter.core.Environment;
import com.janetfilter.core.commons.DebugInfo;
import com.janetfilter.core.enums.RuleType;
import com.janetfilter.core.models.FilterRule;
import com.janetfilter.core.plugin.MyTransformer;
import com.janetfilter.core.plugin.PluginConfig;
import com.janetfilter.core.plugin.PluginEntry;
import java.util.ArrayList;
import java.util.List;

public class ValidPlugin implements PluginEntry {

  private final List<MyTransformer> transformers = new ArrayList<>();

  @Override
  public void init(Environment environment, PluginConfig config) {
    List<FilterRule> filtersRules = config.getBySection("Methods");
    for (FilterRule rule : filtersRules) {
      String ruleStr = rule.getRule();
      String[] methodInfo = ruleStr.split("\\|", 3);
      if (methodInfo.length != 3) {
        DebugInfo.output("Invalid configuration: " + rule
            + ", skipped. should be <clsname>|<methodname>|<method desc");
      }
      String className = methodInfo[0].trim();
      String methodName = methodInfo[1].trim();
      String methodDesc = methodInfo[2].trim();

      switch (rule.getType()){
        case EQUAL:
          transformers.add(new ValidationTransformer(className, methodName, methodDesc));
          break;
        case REGEXP:
          transformers.add(new ValidationManagerTransformer(className, methodName, methodDesc));
          break;
        default:
      }
    }
  }

  @Override
  public String getName() {
    return "SgValid";
  }

  @Override
  public String getAuthor() {
    return "mz";
  }

  @Override
  public String getVersion() {
    return "v1.0.0";
  }

  @Override
  public String getDescription() {
    return "you know if you know, otherwise, no need to know";
  }

  @Override
  public List<MyTransformer> getTransformers() {
    return this.transformers;
  }
}
