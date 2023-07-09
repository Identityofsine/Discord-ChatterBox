package Commands.Permission;

public class Role {

    private String role_name;

    public Role(String name) {
        this.role_name = name;
    }

    public String getRoleName() {
        return this.role_name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Role){
            return ((Role)obj).getRoleName().equalsIgnoreCase(this.role_name);
        } else if(obj instanceof String) {
            return ((String)obj).equalsIgnoreCase(this.role_name);
        } else if(obj instanceof net.dv8tion.jda.api.entities.Role) {
            try {
                return ((net.dv8tion.jda.api.entities.Role)obj).getName().equalsIgnoreCase(this.role_name);
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
