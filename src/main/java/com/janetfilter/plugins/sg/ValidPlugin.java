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
      if (rule.getType() != RuleType.EQUAL) {
        continue;
      }
      String ruleStr = rule.getRule();
      String[] methodInfo = ruleStr.split("\\|", 3);
      if (methodInfo.length != 3) {
        DebugInfo.output("Invalid configuration: " + rule
            + ", skipped. should be <clsname>|<methodname>|<method desc");
      }
      DebugInfo.debug(
          String.format("add ValidTransformer for %s %s %s", methodInfo[0], methodInfo[1],
              methodInfo[2]));
      transformers.add(new ValidationTransformer(methodInfo[0].trim(), methodInfo[1].trim(),
          methodInfo[2].trim()));
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
