package blog.areas.user.viewModel;

public class UserViewModelImpl implements UserViewModel{
    private Integer id;
    private String email;
    private String name;
    private byte[] picture;

    public UserViewModelImpl(Integer id, String email, String name, byte[] picture) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public byte[] getPicture() {
        return this.picture;
    }
}
