// This is a generated file. Not intended for manual editing.
package org.vyperlang.plugin.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface VyperEventLogExpression extends VyperExpression {

  @NotNull
  VyperFunctionCallArguments getFunctionCallArguments();

  @NotNull
  VyperVarLiteral getVarLiteral();

  @Nullable
  PsiElement getIdentifier();

}
