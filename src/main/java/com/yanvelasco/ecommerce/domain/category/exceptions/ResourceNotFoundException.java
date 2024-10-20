package  com.yanvelasco.ecommerce.domain.category.exceptions;

import java.util.UUID;
import lombok.Getter;


@Getter
public class ResourceNotFoundException extends RuntimeException {
    private  String rsourcename;
    private String fieldname;
    private String field;
    private UUID fieldId;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String rsourcename, String fieldname, String field) {
        super(String.format("%s not found with %s : %s", rsourcename,field, fieldname));
        this.rsourcename = rsourcename;
        this.field = field;
        this.fieldname = fieldname;
    }

    public ResourceNotFoundException(String rsourcename, String field, UUID fieldId) {
        super(String.format("%s not found with %s : %s", rsourcename,field, fieldId));
        this.rsourcename = rsourcename;
        this.field = field;
        this.fieldId = fieldId;
    }

}