public class H5VersionResponse {

    /**
     * enable : 1
     * force : 0
     * key : 2ab9267f4048d4b1d7f192d86c04e5b2
     * url : http://static.longyy.dev.anhouse.com.cn/ztxs/version/offline_2ab9267f4048d4b1d7f192d86c04e5b2.zip
     * txt : 请升级
     */

    public int enable;
    public int force;
    public String txt;
    public String key;
    public String url;

    @Override
    public String toString() {
        return "H5VersionResponse{" +
                "enable=" + enable +
                ", force=" + force +
                ", txt='" + txt + '\'' +
                ", key='" + key + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
