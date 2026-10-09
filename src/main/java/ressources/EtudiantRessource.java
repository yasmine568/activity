package ressources;

import entities.Etudiant;
import entities.EtudiantList;
import entities.Option;
import metiers.EtudiantBusiness;
import metiers.OptionBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/etudiants")
public class EtudiantRessource {

    private final EtudiantBusiness etudiantBusiness = new EtudiantBusiness();
    private final OptionBusiness optionBusiness = new OptionBusiness();

    // 1) POST /etudiants
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEtudiant(Etudiant etudiant) {
        if (etudiant == null || etudiant.getOption() == null
                || optionBusiness.getOptionByCode(etudiant.getOption().getCodeOption()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        etudiantBusiness.addEtudiant(etudiant);
        return Response.ok(etudiant).build();
    }

    // 2) GET /etudiants
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Etudiant> getEtudiants() {
        return etudiantBusiness.getAllEtudiants();
    }

    // 6) GET /etudiants/option?codeOption=1  (réponse en XML)
    @GET
    @Path("/option")
    @Produces(MediaType.APPLICATION_XML)
    public Response getByOption(@QueryParam("codeOption") int codeOption) {
        Option option = optionBusiness.getOptionByCode(codeOption);
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        List<Etudiant> liste = etudiantBusiness.getEtudiantsByOption(option);
        return Response.ok(new EtudiantList(liste)).build();
    }

    // 3) GET /etudiants/{id}
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        Etudiant e = etudiantBusiness.getEtudiantByIdentifiant(id);
        if (e == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(e).build();
    }

    // 4) DELETE /etudiants/{id}
    @DELETE
    @Path("/{id}")
    public Response deleteEtudiant(@PathParam("id") String id) {
        if (etudiantBusiness.deleteEtudiant(id)) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // 5) PUT /etudiants/{id}
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEtudiant(@PathParam("id") String id, Etudiant etudiant) {
        if (etudiant == null || etudiant.getOption() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Option option = optionBusiness.getOptionByCode(etudiant.getOption().getCodeOption());
        if (option == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        etudiant.setOption(option);
        if (etudiantBusiness.updateEtudiant(id, etudiant)) {
            return Response.ok(etudiant).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}