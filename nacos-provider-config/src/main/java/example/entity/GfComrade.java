package example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@TableName("gf_comrade")
public class GfComrade {

    private Integer id;
    private String memberId;
    private String discordName;
    private BigDecimal ruleDonate;
    private Boolean peculiarChair;
    private BigDecimal vogInvestor;
    private BigDecimal loaner;
    private BigDecimal gdollarInvestor;
    @TableField(value = "g1_investor")
    private BigDecimal g1Investor;
    private Boolean parade;
    private Boolean drinkTea;
    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getDiscordName() {
        return discordName;
    }

    public void setDiscordName(String discordName) {
        this.discordName = discordName;
    }

    public BigDecimal getRuleDonate() {
        return ruleDonate;
    }

    public void setRuleDonate(BigDecimal ruleDonate) {
        this.ruleDonate = ruleDonate;
    }

    public Boolean getPeculiarChair() {
        return peculiarChair;
    }

    public void setPeculiarChair(Boolean peculiarChair) {
        this.peculiarChair = peculiarChair;
    }

    public BigDecimal getVogInvestor() {
        return vogInvestor;
    }

    public void setVogInvestor(BigDecimal vogInvestor) {
        this.vogInvestor = vogInvestor;
    }

    public BigDecimal getLoaner() {
        return loaner;
    }

    public void setLoaner(BigDecimal loaner) {
        this.loaner = loaner;
    }

    public BigDecimal getGdollarInvestor() {
        return gdollarInvestor;
    }

    public void setGdollarInvestor(BigDecimal gdollarInvestor) {
        this.gdollarInvestor = gdollarInvestor;
    }

    public BigDecimal getG1Investor() {
        return g1Investor;
    }

    public void setG1Investor(BigDecimal g1Investor) {
        this.g1Investor = g1Investor;
    }

    public Boolean getParade() {
        return parade;
    }

    public void setParade(Boolean parade) {
        this.parade = parade;
    }

    public Boolean getDrinkTea() {
        return drinkTea;
    }

    public void setDrinkTea(Boolean drinkTea) {
        this.drinkTea = drinkTea;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "GfComrade{" +
                "id=" + id +
                ", memberId='" + memberId + '\'' +
                ", discordName='" + discordName + '\'' +
                ", ruleDonate=" + ruleDonate +
                ", peculiarChair=" + peculiarChair +
                ", vogInvestor=" + vogInvestor +
                ", loaner=" + loaner +
                ", gdollarInvestor=" + gdollarInvestor +
                ", g1Investor=" + g1Investor +
                ", parade=" + parade +
                ", drinkTea=" + drinkTea +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
