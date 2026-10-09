package ressources;

import entities.Option;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/options")
@Produces(MediaType.APPLICATION_JSON)
public class OptionRessource {

        private final OptionBusiness optionBusiness = new OptionBusiness();

        // 1) POST /options
        @POST
        @Consumes(MediaType.APPLICATION_JSON)
        public Response addOption(Option option) {
                if (option != null && optionBusiness.addOption(option)) {
                        return Response.ok(option).build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 2) GET /options   et   3) GET /options?domaine=Mathématiques
        @GET
        public List<Option> getOptions(@QueryParam("domaine") String domaine) {
                if (domaine == null || domaine.isEmpty()) {
                        return optionBusiness.getListeOptions();
                }
                return optionBusiness.getOptionsByDomaine(domaine);
        }

        // 4) DELETE /options/{code}
        @DELETE
        @Path("/{code}")
        public Response deleteOption(@PathParam("code") int code) {
                if (optionBusiness.deleteOption(code)) {
                        return Response.noContent().build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 5) PUT /options/{code}
        @PUT
        @Path("/{code}")
        @Consumes(MediaType.APPLICATION_JSON)
        public Response updateOption(@PathParam("code") int code, Option option) {
                if (optionBusiness.updateOption(code, option)) {
                        return Response.ok(option).build();
                }
                return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 6) GET /options/{code}
        @GET
        @Path("/{code}")
        public Response getByCode(@PathParam("code") int code) {
                Option option = optionBusiness.getOptionByCode(code);
                if (option == null) {
                        return Response.status(Response.Status.NOT_FOUND).build();
                }
                return Response.ok(option).build();
        }
}