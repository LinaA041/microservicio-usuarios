package co.analisys.biblioteca.controller;


import co.analisys.biblioteca.model.Email;
import co.analisys.biblioteca.model.Usuario;
import co.analisys.biblioteca.model.UsuarioId;
import co.analisys.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuarios", description = "Operaciones de consulta y actualización de usuarios de la biblioteca")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Obtener un usuario por ID",
            description = "Retorna la información completa de un usuario: nombre, email, dirección y credenciales. Requiere rol ROLE_LIBRARIAN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere ROLE_LIBRARIAN", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public Usuario obtenerUsuario(
            @Parameter(description = "ID único del usuario", required = true, example = "U001")
            @PathVariable String id) {
        return usuarioService.obtenerUsuario(new UsuarioId(id));
    }


    @Operation(
            summary = "Cambiar email de un usuario",
            description = "Actualiza la dirección de correo electrónico de un usuario existente. Requiere rol ROLE_LIBRARIAN.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email actualizado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado — se requiere ROLE_LIBRARIAN", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    @PutMapping("/{id}/email")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public void cambiarEmail(
            @Parameter(description = "ID único del usuario", required = true, example = "U001")
            @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo correo electrónico del usuario",
                    required = true,
                    content = @Content(schema = @Schema(implementation = String.class, example = "nuevo@correo.com"))
            )
            @RequestBody String nuevoEmail) {
        usuarioService.cambiarEmailUsuario(new UsuarioId(id), new Email(nuevoEmail));
    }
}
