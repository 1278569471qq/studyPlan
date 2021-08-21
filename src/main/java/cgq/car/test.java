package cgq.car;

/**
 * @author zhangzhenxin03 <zhangzhenxin03@kuaishou.com>
 * Created on 2021-07-25
 */
public class test {
    public static void main(String[] args) {
        String s = "BmvyBY5Lu17cETObZidMDlBH6XIZXSYrxzjvg%2BDs6Fpri1kfgMU5P0QHA4k2EJkfk0K3TbZLeK6U%0AUSK%2FhXB9W%2BXWFKUOdMat%2FSuZdoAMUwdhQyQ2ouEKnAl%2FxRt3dIii1DcmnTr3LSv4DHbYbhnU0s%2Fo%0AZHakXYEDRdMh1xxP%2FKCUqM%2BaaZmXQzM3NUuU%2BFOkhmnmrW4GJgJ%2FQIlXbVoG6iaCxHiVthuBNdW8%0AUqy7n8aGPY%2BOGkJ2ZeodcMt6fx%2FANVjv1%2FBsiuy0aGWpUMSF0uFNVgxVZs9zyLAO1czrX8yEh6bs%0ACLX13A%3D%3D|预订|270000G62606|G626|TNV|BXP|TNV|BXP|19:28|22:32|03:04|N|ABI2l7pqtsvEiQ%2BorvBu1vKMjo3bFX1fEWWmbALUyrg2f14x|20210725|3|V2|01|06|1|0|||||||||||无|无|无||O0M090|OM9|0|1||O019700000M0315500009061200000|0|||||1|#1#0|";
        String[] split = s.split("\\|");
        System.out.println(split[3]);
        System.out.println(split[8]);
        System.out.println(split[9]);
    }
}
