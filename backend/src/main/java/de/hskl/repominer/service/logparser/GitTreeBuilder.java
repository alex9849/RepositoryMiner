package de.hskl.repominer.service.logparser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GitTreeBuilder {
    private final Map<String, TreeNode> hashToCommitMap = new HashMap<>();
    private final Map<String, Set<TreeNode>> hashToChildMap = new HashMap<>();
    private final GitTree tree;
    private boolean built = false;

    public GitTreeBuilder(ParsedCommit rootCommit) {
        TreeNode rootNode = new TreeNode(rootCommit);
        this.tree = new GitTree(rootNode);
        hashToCommitMap.put(rootCommit.hash, rootNode);

        Set<TreeNode> children = hashToChildMap.computeIfAbsent(rootCommit.parentHash, x -> new HashSet<>());
        children.add(rootNode);

        if (rootCommit instanceof ParsedMergeCommit) {
            ParsedMergeCommit pmc = (ParsedMergeCommit) rootCommit;
            Set<TreeNode> sourceChildren = hashToChildMap.computeIfAbsent(pmc.mergeSourceHash, x -> new HashSet<>());
            sourceChildren.add(rootNode);
        }
    }

    public GitTreeBuilder append(ParsedCommit commit) {
        if(built) {
            throw new IllegalStateException("Tree has been built already!");
        }
        TreeNode commitNode = new TreeNode(commit);
        hashToCommitMap.put(commit.hash, commitNode);
        Set<TreeNode> parentChildren = hashToChildMap.computeIfAbsent(commit.parentHash, x -> new HashSet<>());
        parentChildren.add(commitNode);

        if (commit instanceof ParsedMergeCommit) {
            ParsedMergeCommit pmc = (ParsedMergeCommit) commit;
            Set<TreeNode> sourceChildren = hashToChildMap.computeIfAbsent(pmc.mergeSourceHash, x -> new HashSet<>());
            sourceChildren.add(commitNode);
        }

        Set<TreeNode> children = hashToChildMap.get(commit.hash);
        commitNode.getChildren().addAll(children);
        children.forEach(child -> {
            if(child.getCommit() instanceof ParsedMergeCommit) {
                ParsedMergeCommit pmcChild = (ParsedMergeCommit) child.getCommit();
                if(pmcChild.mergeSourceHash.equals(commit.hash)) {
                    child.setSourceTarget(commitNode);
                } else {
                    child.setTargetParent(commitNode);
                }
            } else {
                child.setTargetParent(commitNode);
            }

        });
        return this;
    }

    public GitTree build() {
        return tree;
    }

    public class GitTree {
        private final Map<String, TreeNode> hashToCommitMap = GitTreeBuilder.this.hashToCommitMap;
        private final TreeNode root;

        private GitTree(TreeNode rootNode) {
            this.root = rootNode;
        }

        public TreeNode getCommitNode(String hash) {
            return hashToCommitMap.get(hash);
        }
    }

    public class TreeNode {
        private TreeNode targetParent;
        private TreeNode sourceTarget;
        private final Set<TreeNode> children;
        private final ParsedCommit commit;

        private TreeNode(ParsedCommit commit) {
            this.children = new HashSet<>();
            this.commit = commit;
        }

        public Set<TreeNode> getParents() {
            Set<TreeNode> parents = new HashSet<>();
            if(targetParent != null) {
                parents.add(targetParent);
            }
            if(sourceTarget != null) {
                parents.add(sourceTarget);
            }
            return parents;
        }

        public Set<TreeNode> getChildren() {
            return children;
        }

        public ParsedCommit getCommit() {
            return commit;
        }

        public TreeNode getTargetParent() {
            return targetParent;
        }

        public void setTargetParent(TreeNode targetParent) {
            this.targetParent = targetParent;
        }

        public TreeNode getSourceTarget() {
            return sourceTarget;
        }

        public void setSourceTarget(TreeNode sourceTarget) {
            this.sourceTarget = sourceTarget;
        }

        public boolean isMerge() {
            return sourceTarget != null;
        }

        public boolean isSplit() {
            return this.children.size() > 1;
        }
    }
}
