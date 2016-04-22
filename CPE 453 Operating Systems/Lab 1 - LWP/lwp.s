	.section	__TEXT,__text,regular,pure_instructions
	.macosx_version_min 10, 11
	.globl	_qinsert
	.align	4, 0x90
_qinsert:                               ## @qinsert
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp0:
	.cfi_def_cfa_offset 16
Ltmp1:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp2:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movq	_qhead@GOTPCREL(%rip), %rax
	movl	%edi, -4(%rbp)
	cmpq	$0, (%rax)
	jne	LBB0_2
## BB#1:
	movl	$16, %eax
	movl	%eax, %edi
	callq	_malloc
	movq	_qtail@GOTPCREL(%rip), %rdi
	movq	_qhead@GOTPCREL(%rip), %rcx
	movq	%rax, (%rcx)
	movl	-4(%rbp), %edx
	movq	(%rcx), %rax
	movl	%edx, (%rax)
	movq	(%rcx), %rax
	movq	$0, 8(%rax)
	movq	(%rcx), %rax
	movq	%rax, (%rdi)
	jmp	LBB0_3
LBB0_2:
	movl	$16, %eax
	movl	%eax, %edi
	callq	_malloc
	movq	_qtail@GOTPCREL(%rip), %rdi
	movq	(%rdi), %rcx
	movq	%rax, 8(%rcx)
	movl	-4(%rbp), %edx
	movq	(%rdi), %rax
	movq	8(%rax), %rax
	movl	%edx, (%rax)
	movq	(%rdi), %rax
	movq	8(%rax), %rax
	movq	$0, 8(%rax)
	movq	(%rdi), %rax
	movq	8(%rax), %rax
	movq	%rax, (%rdi)
LBB0_3:
	addq	$16, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_qdelete
	.align	4, 0x90
_qdelete:                               ## @qdelete
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp3:
	.cfi_def_cfa_offset 16
Ltmp4:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp5:
	.cfi_def_cfa_register %rbp
	movq	_qhead@GOTPCREL(%rip), %rax
	movq	(%rax), %rcx
	movl	(%rcx), %edx
	movl	%edx, -4(%rbp)
	movq	(%rax), %rcx
	movq	8(%rcx), %rcx
	movq	%rcx, (%rax)
	movl	-4(%rbp), %eax
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_qremove
	.align	4, 0x90
_qremove:                               ## @qremove
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp6:
	.cfi_def_cfa_offset 16
Ltmp7:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp8:
	.cfi_def_cfa_register %rbp
	subq	$32, %rsp
	movq	_qhead@GOTPCREL(%rip), %rax
	movl	%edi, -8(%rbp)
	movq	(%rax), %rcx
	movq	%rcx, -16(%rbp)
	cmpq	$0, (%rax)
	je	LBB2_3
## BB#1:
	movq	_qhead@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	movl	(%rax), %ecx
	cmpl	-8(%rbp), %ecx
	jne	LBB2_3
## BB#2:
	movq	_qhead@GOTPCREL(%rip), %rax
	movq	(%rax), %rcx
	movq	%rcx, -24(%rbp)
	movq	(%rax), %rcx
	movq	8(%rcx), %rcx
	movq	%rcx, (%rax)
	movq	-24(%rbp), %rax
	movq	%rax, %rdi
	callq	_free
	movl	-8(%rbp), %edx
	movl	%edx, -4(%rbp)
	jmp	LBB2_11
LBB2_3:
	jmp	LBB2_4
LBB2_4:                                 ## =>This Inner Loop Header: Depth=1
	xorl	%eax, %eax
	movb	%al, %cl
	cmpq	$0, -16(%rbp)
	movb	%cl, -25(%rbp)          ## 1-byte Spill
	je	LBB2_6
## BB#5:                                ##   in Loop: Header=BB2_4 Depth=1
	movq	-16(%rbp), %rax
	cmpq	$0, 8(%rax)
	setne	%cl
	movb	%cl, -25(%rbp)          ## 1-byte Spill
LBB2_6:                                 ##   in Loop: Header=BB2_4 Depth=1
	movb	-25(%rbp), %al          ## 1-byte Reload
	testb	$1, %al
	jne	LBB2_7
	jmp	LBB2_10
LBB2_7:                                 ##   in Loop: Header=BB2_4 Depth=1
	movq	-16(%rbp), %rax
	movq	8(%rax), %rax
	movl	(%rax), %ecx
	cmpl	-8(%rbp), %ecx
	jne	LBB2_9
## BB#8:
	movq	-16(%rbp), %rax
	movq	8(%rax), %rax
	movq	%rax, -24(%rbp)
	movq	-16(%rbp), %rax
	movq	8(%rax), %rax
	movq	8(%rax), %rax
	movq	-16(%rbp), %rcx
	movq	%rax, 8(%rcx)
	movq	-24(%rbp), %rax
	movq	%rax, %rdi
	callq	_free
	movl	-8(%rbp), %edx
	movl	%edx, -4(%rbp)
	jmp	LBB2_11
LBB2_9:                                 ##   in Loop: Header=BB2_4 Depth=1
	movq	-16(%rbp), %rax
	movq	8(%rax), %rax
	movq	%rax, -16(%rbp)
	jmp	LBB2_4
LBB2_10:
	movl	$-1, -4(%rbp)
LBB2_11:
	movl	-4(%rbp), %eax
	addq	$32, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_qprint
	.align	4, 0x90
_qprint:                                ## @qprint
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp9:
	.cfi_def_cfa_offset 16
Ltmp10:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp11:
	.cfi_def_cfa_register %rbp
	subq	$32, %rsp
	leaq	L_.str(%rip), %rdi
	movq	_qhead@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	movq	%rax, -8(%rbp)
	movb	$0, %al
	callq	_printf
	movl	%eax, -12(%rbp)         ## 4-byte Spill
LBB3_1:                                 ## =>This Inner Loop Header: Depth=1
	cmpq	$0, -8(%rbp)
	je	LBB3_3
## BB#2:                                ##   in Loop: Header=BB3_1 Depth=1
	leaq	L_.str.1(%rip), %rdi
	movq	-8(%rbp), %rax
	movl	(%rax), %esi
	movb	$0, %al
	callq	_printf
	movq	-8(%rbp), %rdi
	movq	8(%rdi), %rdi
	movq	%rdi, -8(%rbp)
	movl	%eax, -16(%rbp)         ## 4-byte Spill
	jmp	LBB3_1
LBB3_3:
	leaq	L_.str.2(%rip), %rdi
	movb	$0, %al
	callq	_printf
	movl	%eax, -20(%rbp)         ## 4-byte Spill
	addq	$32, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_qrewind
	.align	4, 0x90
_qrewind:                               ## @qrewind
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp12:
	.cfi_def_cfa_offset 16
Ltmp13:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp14:
	.cfi_def_cfa_register %rbp
	movq	_qhead@GOTPCREL(%rip), %rax
	movq	(%rax), %rcx
	movq	%rcx, -8(%rbp)
	cmpq	$0, (%rax)
	je	LBB4_2
## BB#1:
	movq	_qhead@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	cmpq	$0, 8(%rax)
	jne	LBB4_3
LBB4_2:
	jmp	LBB4_10
LBB4_3:
	jmp	LBB4_4
LBB4_4:                                 ## =>This Inner Loop Header: Depth=1
	xorl	%eax, %eax
	movb	%al, %cl
	cmpq	$0, -8(%rbp)
	movb	%cl, -9(%rbp)           ## 1-byte Spill
	je	LBB4_7
## BB#5:                                ##   in Loop: Header=BB4_4 Depth=1
	xorl	%eax, %eax
	movb	%al, %cl
	movq	-8(%rbp), %rdx
	cmpq	$0, 8(%rdx)
	movb	%cl, -9(%rbp)           ## 1-byte Spill
	je	LBB4_7
## BB#6:                                ##   in Loop: Header=BB4_4 Depth=1
	movq	-8(%rbp), %rax
	movq	8(%rax), %rax
	cmpq	$0, 8(%rax)
	setne	%cl
	movb	%cl, -9(%rbp)           ## 1-byte Spill
LBB4_7:                                 ##   in Loop: Header=BB4_4 Depth=1
	movb	-9(%rbp), %al           ## 1-byte Reload
	testb	$1, %al
	jne	LBB4_8
	jmp	LBB4_9
LBB4_8:                                 ##   in Loop: Header=BB4_4 Depth=1
	movq	-8(%rbp), %rax
	movq	8(%rax), %rax
	movq	%rax, -8(%rbp)
	jmp	LBB4_4
LBB4_9:
	movq	_qtail@GOTPCREL(%rip), %rax
	movq	_qhead@GOTPCREL(%rip), %rcx
	movq	(%rcx), %rdx
	movq	(%rax), %rsi
	movq	%rdx, 8(%rsi)
	movq	(%rax), %rdx
	movq	%rdx, (%rcx)
	movq	-8(%rbp), %rcx
	movq	%rcx, (%rax)
	movq	(%rax), %rax
	movq	$0, 8(%rax)
LBB4_10:
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_updateProcessTable
	.align	4, 0x90
_updateProcessTable:                    ## @updateProcessTable
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp15:
	.cfi_def_cfa_offset 16
Ltmp16:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp17:
	.cfi_def_cfa_register %rbp
	movl	%edi, -4(%rbp)
	movl	-4(%rbp), %edi
	movl	%edi, -8(%rbp)
LBB5_1:                                 ## =>This Inner Loop Header: Depth=1
	movl	-8(%rbp), %eax
	movl	_lwp_procs(%rip), %ecx
	addl	$1, %ecx
	cmpl	%ecx, %eax
	jge	LBB5_4
## BB#2:                                ##   in Loop: Header=BB5_1 Depth=1
	movq	_lwp_ptable@GOTPCREL(%rip), %rax
	movslq	-8(%rbp), %rcx
	shlq	$5, %rcx
	movq	%rax, %rdx
	addq	%rcx, %rdx
	movl	-8(%rbp), %esi
	addl	$1, %esi
	movslq	%esi, %rcx
	shlq	$5, %rcx
	addq	%rcx, %rax
	movq	(%rax), %rcx
	movq	%rcx, (%rdx)
	movq	8(%rax), %rcx
	movq	%rcx, 8(%rdx)
	movq	16(%rax), %rcx
	movq	%rcx, 16(%rdx)
	movq	24(%rax), %rax
	movq	%rax, 24(%rdx)
## BB#3:                                ##   in Loop: Header=BB5_1 Depth=1
	movl	-8(%rbp), %eax
	addl	$1, %eax
	movl	%eax, -8(%rbp)
	jmp	LBB5_1
LBB5_4:
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_roundRobin
	.align	4, 0x90
_roundRobin:                            ## @roundRobin
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp18:
	.cfi_def_cfa_offset 16
Ltmp19:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp20:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	callq	_qdelete
	movl	%eax, -4(%rbp)
	movl	-4(%rbp), %edi
	callq	_qinsert
	movl	-4(%rbp), %eax
	addq	$16, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_setNextScheduled
	.align	4, 0x90
_setNextScheduled:                      ## @setNextScheduled
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp21:
	.cfi_def_cfa_offset 16
Ltmp22:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp23:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movl	$-1, -4(%rbp)
	cmpl	$0, _lwp_procs(%rip)
	jne	LBB7_2
## BB#1:
	leaq	L_.str.3(%rip), %rdi
	movb	$0, %al
	callq	_printf
	movl	%eax, -8(%rbp)          ## 4-byte Spill
	jmp	LBB7_7
LBB7_2:
	movq	_schedFun@GOTPCREL(%rip), %rax
	cmpq	$0, (%rax)
	je	LBB7_4
## BB#3:
	movq	_schedFun@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	cmpq	$0, (%rax)
	jne	LBB7_5
LBB7_4:
	callq	_roundRobin
	movl	%eax, -4(%rbp)
	jmp	LBB7_6
LBB7_5:
	movq	_schedFun@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	callq	*(%rax)
	movl	%eax, -4(%rbp)
LBB7_6:
	jmp	LBB7_7
LBB7_7:
	movq	_currentLWP@GOTPCREL(%rip), %rax
	movq	_lwp_ptable@GOTPCREL(%rip), %rcx
	movslq	-4(%rbp), %rdx
	shlq	$5, %rdx
	addq	%rdx, %rcx
	movq	%rcx, (%rax)
	movl	-4(%rbp), %esi
	movl	%esi, _lwp_running(%rip)
	addq	$16, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_new_lwp
	.align	4, 0x90
_new_lwp:                               ## @new_lwp
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp24:
	.cfi_def_cfa_offset 16
Ltmp25:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp26:
	.cfi_def_cfa_register %rbp
	subq	$96, %rsp
	movq	%rdi, -16(%rbp)
	movq	%rsi, -24(%rbp)
	movq	%rdx, -32(%rbp)
	cmpl	$30, _lwp_procs(%rip)
	jl	LBB8_2
## BB#1:
	movl	$-1, -4(%rbp)
	jmp	LBB8_7
LBB8_2:
	movq	-32(%rbp), %rax
	shlq	$3, %rax
	movq	%rax, %rdi
	callq	_malloc
	leaq	_lwp_exit(%rip), %rdi
	movq	%rax, -88(%rbp)
	movq	-88(%rbp), %rax
	movq	-32(%rbp), %rcx
	shlq	$3, %rcx
	addq	%rcx, %rax
	movq	%rax, -72(%rbp)
	movq	-24(%rbp), %rax
	movq	-72(%rbp), %rcx
	movq	%rax, (%rcx)
	movq	-72(%rbp), %rax
	addq	$-8, %rax
	movq	%rax, -72(%rbp)
	movq	-72(%rbp), %rax
	movq	%rdi, (%rax)
	movq	-72(%rbp), %rax
	addq	$-8, %rax
	movq	%rax, -72(%rbp)
	movq	-16(%rbp), %rax
	movq	-72(%rbp), %rcx
	movq	%rax, (%rcx)
	movq	-72(%rbp), %rax
	addq	$-8, %rax
	movq	%rax, -72(%rbp)
	movq	-72(%rbp), %rax
	movq	$7, (%rax)
	movq	-72(%rbp), %rax
	movq	%rax, -80(%rbp)
	movq	-72(%rbp), %rax
	addq	$-8, %rax
	movq	%rax, -72(%rbp)
	movl	$0, -92(%rbp)
LBB8_3:                                 ## =>This Inner Loop Header: Depth=1
	cmpl	$6, -92(%rbp)
	jge	LBB8_6
## BB#4:                                ##   in Loop: Header=BB8_3 Depth=1
	movl	-92(%rbp), %eax
	addl	$1, %eax
	movslq	%eax, %rcx
	movq	-72(%rbp), %rdx
	movq	%rcx, (%rdx)
	movq	-72(%rbp), %rcx
	addq	$-8, %rcx
	movq	%rcx, -72(%rbp)
## BB#5:                                ##   in Loop: Header=BB8_3 Depth=1
	movl	-92(%rbp), %eax
	addl	$1, %eax
	movl	%eax, -92(%rbp)
	jmp	LBB8_3
LBB8_6:
	movq	_lwp_ptable@GOTPCREL(%rip), %rax
	movq	-80(%rbp), %rcx
	movq	-72(%rbp), %rdx
	movq	%rcx, (%rdx)
	movl	_lwp_procs(%rip), %esi
	addl	$1, %esi
	movl	%esi, _lwp_procs(%rip)
	movslq	%esi, %rcx
	movq	%rcx, -64(%rbp)
	movq	-88(%rbp), %rcx
	movq	%rcx, -56(%rbp)
	movq	-32(%rbp), %rcx
	movq	%rcx, -48(%rbp)
	movq	-72(%rbp), %rcx
	movq	%rcx, -40(%rbp)
	movq	-64(%rbp), %rcx
	subq	$1, %rcx
	shlq	$5, %rcx
	addq	%rcx, %rax
	movq	-64(%rbp), %rcx
	movq	%rcx, (%rax)
	movq	-56(%rbp), %rcx
	movq	%rcx, 8(%rax)
	movq	-48(%rbp), %rcx
	movq	%rcx, 16(%rax)
	movq	-40(%rbp), %rcx
	movq	%rcx, 24(%rax)
	movq	-64(%rbp), %rax
	subq	$1, %rax
	movl	%eax, %esi
	movl	%esi, %edi
	callq	_qinsert
	movq	-64(%rbp), %rax
	movl	%eax, %esi
	movl	%esi, -4(%rbp)
LBB8_7:
	movl	-4(%rbp), %eax
	addq	$96, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_exit
	.align	4, 0x90
_lwp_exit:                              ## @lwp_exit
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp27:
	.cfi_def_cfa_offset 16
Ltmp28:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp29:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movq	_currentLWP@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	movq	8(%rax), %rax
	movq	%rax, %rdi
	callq	_free
	movl	_lwp_procs(%rip), %ecx
	addl	$-1, %ecx
	movl	%ecx, _lwp_procs(%rip)
	movl	%ecx, %edi
	callq	_qremove
	cmpl	$0, _lwp_procs(%rip)
	movl	%eax, -4(%rbp)          ## 4-byte Spill
	jne	LBB9_2
## BB#1:
	callq	_lwp_stop
	jmp	LBB9_6
LBB9_2:
	movq	_schedFun@GOTPCREL(%rip), %rax
	cmpq	$0, (%rax)
	je	LBB9_4
## BB#3:
	movq	_schedFun@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	cmpq	$0, (%rax)
	jne	LBB9_5
LBB9_4:
	callq	_qrewind
LBB9_5:
	movl	_lwp_running(%rip), %edi
	callq	_updateProcessTable
	callq	_setNextScheduled
	movq	_currentLWP@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	movq	24(%rax), %rax
	## InlineAsm Start
	movq	%rax, %rsp
	## InlineAsm End
	## InlineAsm Start
	popq	%rbp
	## InlineAsm End
	## InlineAsm Start
	popq	%r15
	## InlineAsm End
	## InlineAsm Start
	popq	%r14
	## InlineAsm End
	## InlineAsm Start
	popq	%r13
	## InlineAsm End
	## InlineAsm Start
	popq	%r12
	## InlineAsm End
	## InlineAsm Start
	popq	%r11
	## InlineAsm End
	## InlineAsm Start
	popq	%r10
	## InlineAsm End
	## InlineAsm Start
	popq	%r9
	## InlineAsm End
	## InlineAsm Start
	popq	%r8
	## InlineAsm End
	## InlineAsm Start
	popq	%rdi
	## InlineAsm End
	## InlineAsm Start
	popq	%rsi
	## InlineAsm End
	## InlineAsm Start
	popq	%rdx
	## InlineAsm End
	## InlineAsm Start
	popq	%rcx
	## InlineAsm End
	## InlineAsm Start
	popq	%rbx
	## InlineAsm End
	## InlineAsm Start
	popq	%rax
	## InlineAsm End
	## InlineAsm Start
	movq	%rbp, %rsp
	## InlineAsm End
LBB9_6:
	addq	$16, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_getpid
	.align	4, 0x90
_lwp_getpid:                            ## @lwp_getpid
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp30:
	.cfi_def_cfa_offset 16
Ltmp31:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp32:
	.cfi_def_cfa_register %rbp
	cmpl	$-1, _lwp_running(%rip)
	jle	LBB10_2
## BB#1:
	movq	_lwp_ptable@GOTPCREL(%rip), %rax
	movslq	_lwp_running(%rip), %rcx
	shlq	$5, %rcx
	addq	%rcx, %rax
	movq	(%rax), %rax
	movl	%eax, %edx
	movl	%edx, -4(%rbp)
	jmp	LBB10_3
LBB10_2:
	movl	$0, -4(%rbp)
LBB10_3:
	movl	-4(%rbp), %eax
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_yield
	.align	4, 0x90
_lwp_yield:                             ## @lwp_yield
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp33:
	.cfi_def_cfa_offset 16
Ltmp34:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp35:
	.cfi_def_cfa_register %rbp
	## InlineAsm Start
	pushq	%rax
	## InlineAsm End
	## InlineAsm Start
	pushq	%rbx
	## InlineAsm End
	## InlineAsm Start
	pushq	%rcx
	## InlineAsm End
	## InlineAsm Start
	pushq	%rdx
	## InlineAsm End
	## InlineAsm Start
	pushq	%rsi
	## InlineAsm End
	## InlineAsm Start
	pushq	%rdi
	## InlineAsm End
	## InlineAsm Start
	pushq	%r8
	## InlineAsm End
	## InlineAsm Start
	pushq	%r9
	## InlineAsm End
	## InlineAsm Start
	pushq	%r10
	## InlineAsm End
	## InlineAsm Start
	pushq	%r11
	## InlineAsm End
	## InlineAsm Start
	pushq	%r12
	## InlineAsm End
	## InlineAsm Start
	pushq	%r13
	## InlineAsm End
	## InlineAsm Start
	pushq	%r14
	## InlineAsm End
	## InlineAsm Start
	pushq	%r15
	## InlineAsm End
	## InlineAsm Start
	pushq	%rbp
	## InlineAsm End
	movq	_currentLWP@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	## InlineAsm Start
	movq	%rsp, %rcx
	## InlineAsm End
	movq	%rcx, 24(%rax)
	callq	_setNextScheduled
	movq	_currentLWP@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	movq	24(%rax), %rax
	## InlineAsm Start
	movq	%rax, %rsp
	## InlineAsm End
	## InlineAsm Start
	popq	%rbp
	## InlineAsm End
	## InlineAsm Start
	popq	%r15
	## InlineAsm End
	## InlineAsm Start
	popq	%r14
	## InlineAsm End
	## InlineAsm Start
	popq	%r13
	## InlineAsm End
	## InlineAsm Start
	popq	%r12
	## InlineAsm End
	## InlineAsm Start
	popq	%r11
	## InlineAsm End
	## InlineAsm Start
	popq	%r10
	## InlineAsm End
	## InlineAsm Start
	popq	%r9
	## InlineAsm End
	## InlineAsm Start
	popq	%r8
	## InlineAsm End
	## InlineAsm Start
	popq	%rdi
	## InlineAsm End
	## InlineAsm Start
	popq	%rsi
	## InlineAsm End
	## InlineAsm Start
	popq	%rdx
	## InlineAsm End
	## InlineAsm Start
	popq	%rcx
	## InlineAsm End
	## InlineAsm Start
	popq	%rbx
	## InlineAsm End
	## InlineAsm Start
	popq	%rax
	## InlineAsm End
	## InlineAsm Start
	movq	%rbp, %rsp
	## InlineAsm End
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_start
	.align	4, 0x90
_lwp_start:                             ## @lwp_start
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp36:
	.cfi_def_cfa_offset 16
Ltmp37:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp38:
	.cfi_def_cfa_register %rbp
	cmpl	$0, _lwp_procs(%rip)
	jne	LBB12_2
## BB#1:
	jmp	LBB12_3
LBB12_2:
	## InlineAsm Start
	pushq	%rax
	## InlineAsm End
	## InlineAsm Start
	pushq	%rbx
	## InlineAsm End
	## InlineAsm Start
	pushq	%rcx
	## InlineAsm End
	## InlineAsm Start
	pushq	%rdx
	## InlineAsm End
	## InlineAsm Start
	pushq	%rsi
	## InlineAsm End
	## InlineAsm Start
	pushq	%rdi
	## InlineAsm End
	## InlineAsm Start
	pushq	%r8
	## InlineAsm End
	## InlineAsm Start
	pushq	%r9
	## InlineAsm End
	## InlineAsm Start
	pushq	%r10
	## InlineAsm End
	## InlineAsm Start
	pushq	%r11
	## InlineAsm End
	## InlineAsm Start
	pushq	%r12
	## InlineAsm End
	## InlineAsm Start
	pushq	%r13
	## InlineAsm End
	## InlineAsm Start
	pushq	%r14
	## InlineAsm End
	## InlineAsm Start
	pushq	%r15
	## InlineAsm End
	## InlineAsm Start
	pushq	%rbp
	## InlineAsm End
	movq	_driverStackPointer@GOTPCREL(%rip), %rax
	## InlineAsm Start
	movq	%rsp, %rcx
	## InlineAsm End
	movq	%rcx, (%rax)
	callq	_setNextScheduled
	movq	_currentLWP@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	movq	24(%rax), %rax
	## InlineAsm Start
	movq	%rax, %rsp
	## InlineAsm End
	## InlineAsm Start
	popq	%rbp
	## InlineAsm End
	## InlineAsm Start
	popq	%r15
	## InlineAsm End
	## InlineAsm Start
	popq	%r14
	## InlineAsm End
	## InlineAsm Start
	popq	%r13
	## InlineAsm End
	## InlineAsm Start
	popq	%r12
	## InlineAsm End
	## InlineAsm Start
	popq	%r11
	## InlineAsm End
	## InlineAsm Start
	popq	%r10
	## InlineAsm End
	## InlineAsm Start
	popq	%r9
	## InlineAsm End
	## InlineAsm Start
	popq	%r8
	## InlineAsm End
	## InlineAsm Start
	popq	%rdi
	## InlineAsm End
	## InlineAsm Start
	popq	%rsi
	## InlineAsm End
	## InlineAsm Start
	popq	%rdx
	## InlineAsm End
	## InlineAsm Start
	popq	%rcx
	## InlineAsm End
	## InlineAsm Start
	popq	%rbx
	## InlineAsm End
	## InlineAsm Start
	popq	%rax
	## InlineAsm End
	## InlineAsm Start
	movq	%rbp, %rsp
	## InlineAsm End
LBB12_3:
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_stop
	.align	4, 0x90
_lwp_stop:                              ## @lwp_stop
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp39:
	.cfi_def_cfa_offset 16
Ltmp40:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp41:
	.cfi_def_cfa_register %rbp
	movq	_driverStackPointer@GOTPCREL(%rip), %rax
	movq	(%rax), %rax
	## InlineAsm Start
	movq	%rax, %rsp
	## InlineAsm End
	## InlineAsm Start
	popq	%rbp
	## InlineAsm End
	## InlineAsm Start
	popq	%r15
	## InlineAsm End
	## InlineAsm Start
	popq	%r14
	## InlineAsm End
	## InlineAsm Start
	popq	%r13
	## InlineAsm End
	## InlineAsm Start
	popq	%r12
	## InlineAsm End
	## InlineAsm Start
	popq	%r11
	## InlineAsm End
	## InlineAsm Start
	popq	%r10
	## InlineAsm End
	## InlineAsm Start
	popq	%r9
	## InlineAsm End
	## InlineAsm Start
	popq	%r8
	## InlineAsm End
	## InlineAsm Start
	popq	%rdi
	## InlineAsm End
	## InlineAsm Start
	popq	%rsi
	## InlineAsm End
	## InlineAsm Start
	popq	%rdx
	## InlineAsm End
	## InlineAsm Start
	popq	%rcx
	## InlineAsm End
	## InlineAsm Start
	popq	%rbx
	## InlineAsm End
	## InlineAsm Start
	popq	%rax
	## InlineAsm End
	## InlineAsm Start
	movq	%rbp, %rsp
	## InlineAsm End
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_set_scheduler
	.align	4, 0x90
_lwp_set_scheduler:                     ## @lwp_set_scheduler
	.cfi_startproc
## BB#0:
	pushq	%rbp
Ltmp42:
	.cfi_def_cfa_offset 16
Ltmp43:
	.cfi_offset %rbp, -16
	movq	%rsp, %rbp
Ltmp44:
	.cfi_def_cfa_register %rbp
	subq	$16, %rsp
	movq	_schedFun@GOTPCREL(%rip), %rax
	movq	%rdi, -8(%rbp)
	cmpq	$0, (%rax)
	jne	LBB14_2
## BB#1:
	movl	$8, %eax
	movl	%eax, %edi
	callq	_malloc
	movq	_schedFun@GOTPCREL(%rip), %rdi
	movq	%rax, (%rdi)
LBB14_2:
	movq	_schedFun@GOTPCREL(%rip), %rax
	movq	-8(%rbp), %rcx
	movq	(%rax), %rax
	movq	%rcx, (%rax)
	addq	$16, %rsp
	popq	%rbp
	retq
	.cfi_endproc

	.globl	_lwp_procs              ## @lwp_procs
.zerofill __DATA,__common,_lwp_procs,4,2
	.section	__DATA,__data
	.globl	_lwp_running            ## @lwp_running
	.align	2
_lwp_running:
	.long	4294967295              ## 0xffffffff

	.comm	_qhead,8,3              ## @qhead
	.comm	_qtail,8,3              ## @qtail
	.section	__TEXT,__cstring,cstring_literals
L_.str:                                 ## @.str
	.asciz	"Queue: "

L_.str.1:                               ## @.str.1
	.asciz	"%d "

L_.str.2:                               ## @.str.2
	.asciz	"\n"

	.comm	_lwp_ptable,960,4       ## @lwp_ptable
L_.str.3:                               ## @.str.3
	.asciz	"No LWP processes have been created\n"

	.comm	_schedFun,8,3           ## @schedFun
	.comm	_currentLWP,8,3         ## @currentLWP
	.comm	_driverStackPointer,8,3 ## @driverStackPointer

.subsections_via_symbols
