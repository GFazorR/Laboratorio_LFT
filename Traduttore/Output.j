.class public Output 
.super java/lang/Object

.method public <init>()V
 aload_0
 invokenonvirtual java/lang/Object/<init>()V
 return
.end method

.method public static print(I)V
 .limit stack 2
 getstatic java/lang/System/out Ljava/io/PrintStream;
 iload_0 
 invokestatic java/lang/Integer/toString(I)Ljava/lang/String;
 invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
 return
.end method

.method public static read()I
 .limit stack 3
 new java/util/Scanner
 dup
 getstatic java/lang/System/in Ljava/io/InputStream;
 invokespecial java/util/Scanner/<init>(Ljava/io/InputStream;)V
 invokevirtual java/util/Scanner/next()Ljava/lang/String;
 invokestatic java/lang/Integer.parseInt(Ljava/lang/String;)I
 ireturn
.end method

.method public static run()V
 .limit stack 1024
 .limit locals 256
 ldc 10
 istore 0
L1:
 iload 0
 ldc 4
 if_icmplt L3
 goto L4
L3:
 iload 0
 ldc 1
 isub 
 istore 0
 goto L2
L4:
 iload 0
 ldc 10
 if_icmpgt L5
 goto L6
L5:
 iload 0
 invokestatic Output/print(I)V
 goto L2
L6:
L2:
 iload 0
 invokestatic Output/print(I)V
L7:
 invokestatic Output/read()I
 istore 1
L8:
 iload 0
 iload 1
 iadd 
 invokestatic Output/print(I)V
L9:
 ldc 0
 istore 0
L11:
 iload 0
 ldc 4
 if_icmplt L12
 goto L10
L12:
 iload 0
 invokestatic Output/print(I)V
L13:
 iload 0
 ldc 1
 iadd 
 istore 0
 goto L11
L10:
 iload 1
 ldc 10
 iadd 
 invokestatic Output/print(I)V
L14:
L0:
 return
.end method

.method public static main([Ljava/lang/String;)V
 invokestatic Output/run()V
 return
.end method

