// This is a generated file. Not intended for manual editing.
package com.vyperplugin.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.vyperplugin.psi.VyperTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.vyperplugin.psi.*;

public class VyperEventPropertyImpl extends ASTWrapperPsiElement implements VyperEventProperty {

  public VyperEventPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull VyperVisitor visitor) {
    visitor.visitEventProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof VyperVisitor) accept((VyperVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<VyperIndexedData> getIndexedDataList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, VyperIndexedData.class);
  }

  @Override
  @NotNull
  public List<VyperType> getTypeList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, VyperType.class);
  }

}
