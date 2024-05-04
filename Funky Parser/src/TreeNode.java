import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String value;
    private List<TreeNode> children;

    public TreeNode(String value, TreeNode treeNode, TreeNode node) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    public TreeNode(String value, List<TreeNode> children) {
        this.value = value;
        this.children = children;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return toStringHelper(this, 0);
    }

    private String toStringHelper(TreeNode node, int depth) {
        StringBuilder sb = new StringBuilder();
        sb.append("\t".repeat(depth)).append(node.value).append("\n");
        for (TreeNode child : node.children) {
            sb.append(toStringHelper(child, depth + 1));
        }
        return sb.toString();
    }
}
