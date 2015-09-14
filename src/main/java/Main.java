import org.commonjava.aprox.client.core.Aprox;
import org.commonjava.aprox.depgraph.client.DepgraphAproxClientModule;
import org.commonjava.cartographer.graph.discover.patch.DepgraphPatcherConstants;
import org.commonjava.cartographer.request.GraphDescription;
import org.commonjava.cartographer.request.MetadataCollationRequest;
import org.commonjava.cartographer.request.ProjectGraphRequest;
import org.commonjava.cartographer.result.GraphExport;
import org.commonjava.cartographer.result.MetadataCollationEntry;
import org.commonjava.cartographer.result.MetadataCollationResult;
import org.commonjava.maven.atlas.graph.model.EProjectCycle;
import org.commonjava.maven.atlas.graph.rel.ProjectRelationship;
import org.commonjava.maven.atlas.graph.traverse.model.BuildOrder;
import org.commonjava.maven.atlas.ident.ref.ProjectRef;
import org.commonjava.maven.atlas.ident.ref.ProjectVersionRef;
import org.commonjava.maven.atlas.ident.ref.SimpleProjectVersionRef;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dcheung on 21/08/15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DepgraphAproxClientModule mod = new DepgraphAproxClientModule();
        Aprox aprox = new Aprox("http://10.3.8.115/api", mod).connect();
        MetadataCollationRequest req = mod.newMetadataCollationRequest()
                                  .withWorkspaceId("collation")
                                  .withKeys(Arrays.asList("scm-url", "groupId"))
                                  .withResolve(true)
                                  .withSource("group:public")
                                  .withPatcherIds(DepgraphPatcherConstants.ALL_PATCHERS)
                                  .withGraph(mod.newGraphDescription()
                                          .withRoots(
                                                  new SimpleProjectVersionRef("io.fabric8", "fabric-project",
                                                          "1.2.0.redhat-133"))
                                          .withPreset("build-requires")
                                          .build())
                                  .build();

        MetadataCollationResult result = mod.collateMetadata(req);
        for ( MetadataCollationEntry entry : result ) {
            entry.getMetadata().forEach( ( k, v ) -> {
                if (k.equals("groupId")) {
                    System.out.printf("%s\n", v);
                } else {
                    System.out.printf("%s :: ", v);
                }
            } );
        }
    }
}
