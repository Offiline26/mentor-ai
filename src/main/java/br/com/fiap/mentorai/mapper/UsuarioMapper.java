package br.com.fiap.mentorai.mapper;

import br.com.fiap.mentorai.dto.request.CreateUsuarioRequest;
import br.com.fiap.mentorai.dto.request.UpdateUsuarioRequest;
import br.com.fiap.mentorai.dto.response.UsuarioResponse;
import br.com.fiap.mentorai.model.Usuario;
import br.com.fiap.mentorai.model.enums.NivelProficienciaEnum;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class UsuarioMapper {

    private UsuarioMapper() {}

    // -------- Entidade -> DTO --------
    public static UsuarioResponse toDto(Usuario e) {
        if (e == null) return null;

        UsuarioResponse dto = UsuarioResponse.builder()
                .id(e.getId())
                .nome(e.getNome())
                .email(e.getEmail())
                .dataNascimento(e.getDataNascimento())
                .genero(e.getGenero())
                .pais(e.getPais())
                .dataCadastro(e.getDataCadastro())
                .build();

        if (e.getCargo() != null) {
            dto.setIdCargo(e.getCargo().getId());
            dto.setCargo(e.getCargo().getNome());
        }
        if (e.getAreaAtuacao() != null) {
            dto.setIdAreaAtuacao(e.getAreaAtuacao().getId());
            dto.setAreaAtuacao(e.getAreaAtuacao().getNome());
        }
        if (e.getHabilidades() != null) {
            Set<UsuarioResponse.UsuarioHabilidadeItem> hs = e.getHabilidades().stream()
                    .filter(Objects::nonNull)
                    .map(uh -> UsuarioResponse.UsuarioHabilidadeItem.builder()
                            .idHabilidade(uh.getHabilidade() != null ? uh.getHabilidade().getId() : null)
                            .habilidade(uh.getHabilidade() != null ? uh.getHabilidade().getNome() : null)
                            .nivel(uh.getNivelProficiencia() != null ? uh.getNivelProficiencia().getValorDb() : null)
                            .build())
                    .collect(Collectors.toSet());
            dto.setHabilidades(hs);
        }
        if (e.getRotas() != null) {
            Set<UsuarioResponse.UsuarioRotaItem> rs = e.getRotas().stream()
                    .filter(Objects::nonNull)
                    .map(ur -> UsuarioResponse.UsuarioRotaItem.builder()
                            .idRota(ur.getRota() != null ? ur.getRota().getId() : null)
                            .nomeRota(ur.getRota() != null ? ur.getRota().getNomeRota() : null)
                            .progressoPercentual(ur.getProgressoPercentual())
                            .status(ur.getStatus() != null ? ur.getStatus().name() : null)
                            .dataInicio(ur.getDataInicio())
                            .dataConclusao(ur.getDataConclusao())
                            .build())
                    .collect(Collectors.toSet());
            dto.setRotas(rs);
        }
        return dto;
    }

    public static List<UsuarioResponse> toDtoList(List<Usuario> list) {
        return list == null ? List.of() : list.stream()
                .map(UsuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    // -------- CreateReq -> Entidade --------
    public static Usuario toEntity(CreateUsuarioRequest r) {
        if (r == null) return null;
        Usuario e = new Usuario();
        e.setNome(r.getNome());
        e.setEmail(r.getEmail());
        // senhaHash deve ser gerado no service (hash)
        e.setDataNascimento(r.getDataNascimento());
        e.setGenero(r.getGenero());
        e.setPais(r.getPais());
        // cargo e areaAtuacao serão setados no service a partir dos IDs
        // habilidades e rotas idem (service monta UsuarioHabilidade/UsuarioRota)
        return e;
    }

    // -------- UpdateReq -> Entidade (patch) --------
    public static void applyUpdate(UpdateUsuarioRequest r, Usuario e) {
        if (r == null || e == null) return;
        if (r.getNome() != null) e.setNome(r.getNome());
        if (r.getEmail() != null) e.setEmail(r.getEmail());
        if (r.getDataNascimento() != null) e.setDataNascimento(r.getDataNascimento());
        if (r.getGenero() != null) e.setGenero(r.getGenero());
        if (r.getPais() != null) e.setPais(r.getPais());
        // cargo/area/habilidades serão tratados no service
    }

    // -------- Helpers --------
    public static NivelProficienciaEnum nivelFromInt(Integer i) {
        if (i == null) return null;
        return switch (i) {
            case 1 -> NivelProficienciaEnum.INICIANTE;
            case 2 -> NivelProficienciaEnum.BASICO;
            case 3 -> NivelProficienciaEnum.INTERMEDIARIO;
            case 4 -> NivelProficienciaEnum.AVANCADO;
            case 5 -> NivelProficienciaEnum.ESPECIALISTA;
            default -> throw new IllegalArgumentException("Nível inválido: " + i);
        };
    }
}
