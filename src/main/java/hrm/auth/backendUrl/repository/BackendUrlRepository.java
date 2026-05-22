package hrm.auth.backendUrl.repository;

import hrm.auth.backendUrl.entity.BackendUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BackendUrlRepository extends JpaRepository<BackendUrl,Long> {

    @Query("select b from BackendUrl b where " +
            " ( :id is null or b.id=:id ) " +
            " and ( :name is null or b.name=:name ) " +
            "  and ( :httpMethod is null or b.httpMethod=:httpMethod ) ")
    List<BackendUrl> getList(@Param("id") Long id ,
                             @Param("name") String name,
                             @Param("httpMethod") String httpMethod
                             );

    BackendUrl findByNameAndHttpMethod(String s, String get);
}
