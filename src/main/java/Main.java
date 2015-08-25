import org.commonjava.aprox.client.core.Aprox;
import org.commonjava.aprox.depgraph.client.DepgraphAproxClientModule;
import org.commonjava.cartographer.graph.discover.patch.DepgraphPatcherConstants;
import org.commonjava.cartographer.request.ProjectGraphRequest;
import org.commonjava.cartographer.result.GraphExport;
import org.commonjava.maven.atlas.graph.rel.ProjectRelationship;
import org.commonjava.maven.atlas.ident.ref.ProjectVersionRef;

/**
 * Created by dcheung on 21/08/15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DepgraphAproxClientModule mod = new DepgraphAproxClientModule();
        Aprox aprox = new Aprox("magic_ip", mod).connect();
        ProjectGraphRequest req = mod.newProjectGraphRequest()
                .withWorkspaceId("graph-export")
                .withSource("group:public")
                .withPatcherIds(DepgraphPatcherConstants.ALL_PATCHERS)
                .withResolve(true)
                .withGraph(mod.newGraphDescription()
                        .withRoots(new ProjectVersionRef("dom4j",
                                "dom4j",
                                "1.6.1"))
                        .withPreset("requires")
                        .build())
                .build();

        GraphExport export = mod.graph(req);

        for (ProjectRelationship<?> rel : export.getRelationships()) {
            ProjectVersionRef declaring = rel.getDeclaring();
            ProjectVersionRef targeting = rel.getTargetArtifact();

            if (rel.getType().toString().equals("DEPENDENCY")) {
                if (declaring.toString().contains("dom4j:dom4j:1.6.1")) {
                    System.out.printf("Relationship (type: %s) from: %s to: %s\n", rel.getType(), declaring, targeting);
                }
            }
        }
    }
}
