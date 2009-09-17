_SysBase EQU $4 � addtail ;a1=liststruct a0=node �@.L  #4,a1 �~.L  4(a1),D0 �~.L  a0,4(a1) �u     D0,a1 �~.L D0,(a0) �~.l a1,4(a0) �~.L  a0,(a1) � �  � remove ;a0 liststruct a1 node ��.L (A1)+,A0 �~.L (A1),A1 �~.L  A0,(A1) �~.L  A1,4(A0) � �   � init ; a0 liststruct         �~.L  A0,(A0)  ;A0 points to the list header         �@.L  #4,(A0)  ;Bump LH_HEAD(A0) to address of LH_TAIL         �[.L   4(A0)         �~.L  A0,8(A0) � �  MaxIntSgnd EQU $7FFFFFFF AllocPooled EQU -$2c4 FreePooled SET -$2Ca #return0=0 #poolmem=1 #mungwall=1   ;wall is 12bytes before and 16 bytes after               ;it try to show the sourceline of the memalloc               ;also 8 Bytes are cleared if you release mem               ;slowdown is max 20% 32Bytes more mem per alloc is used               ;do a sysjsr $c007 before program end               ;RELEASE MEM IN EXITROUTINE IS NOT CHECKED               ;to check if walls are ok               ;otherwise this is only done when release mem               ; #walldebug=0  ;only activate wall in debug mode (not used now)  ;TODO: implement the wallcheck SysJsr $c007   ��.l #$00,D0  �� ;optimize 4 impossible to work for now compile modfe fpu ffp   �%.b $FF,$FA,$00,$00 ;....   �&.w 3   �%.b $C0,$00,$C0,$08 ;....    maximum tokennum   �&.l 1   �%.l AL_0_BC   �%.b $00,$01 ;..   �%.l AL_0_D0   �&.l 3   �%.l AL_0_268   �&.l 3   �%.l AJL_0_16A   �&.l 4   �%.l AJL_0_19E   �&.l 4   �%.l AJL_0_136   �&.l 4   �%.l AJL_0_150   �&.l 4   �%.l AJL_0_1EA   �&.l 4   �%.l AJL_0_218   �&.l 4   �%.l AJL_0_108   �&.l 4   �%.l allocret  ;alibjsr $c007   �&.l 1 AL_0_BC:   �&.l 3   �%.l AJL_0_F0   �&.l 1 AL_0_D0:   �&.l 3   �%.l AJL_0_108   �&.l 1   �%.b $FF,$FF,$00,$00 ;....   �&.w 1 memlist:   �&.l 1 AL_0_EE:   �&.w 1   ;OK, here's how our mem linking works: ; ;firstdata.l - points to current list header ; ;listheader - 8 bytes. ;00.l - pointer to next listheader ;04.l - first memblock ;08.l last memalloc address ; ;Memblocks are 8 bytes longer than normal ; ;00.l - Next ;04.l - length of memblock + 8 ;08.l - CHIP or fast indicator ;12.l - free ;16...  Mem returned to User. ; ;sorta like this: ; ;firstdata ---list---list---list... ;               !      !      ! ;              mem    mem    mem ;               !      !      ! ;              mem    mem    mem ;               :      :      : ;               :      :      : ; ;first list is always current local one.  ;******************** CODE SECTION ******************** .allocret :;allocate some memory in the local region!   ��.l A0-A1/A6,-(A7)  � #mungwall=1  �=.l #16+16*2,d0  �   �=.l #16,D0  �   �~.l D0,-(A7)   ��.l _SysBase,A6 _AllocMem SET -$C6 � #poolmem=1   �W #1,d1   �P 'chipm   �~.l mempool,a0   �y AllocPooled(a6) �    �y _AllocMem(A6) �   ��.l D0    �H.w 'l1   ��.l D0,A0   ��.l memlist(PC),A1    �@.l #4,a1    !addtail ;  MOVE.l $4(A1),(A0) ;  MOVE.l d0,8(a1) ;  MOVE.l A0,$4(A1)   �~.l (A7)+,12(A0)    ;size was 4 � #mungwall=1   �~.l a0,a1   ��.w #16,a1   �=.l 12(a0),a1        ; add of size   �=.w #16,a0   ;MOVE.l #$DEADDEAD,(a0)+   �~.l 28(a7),(a0)+   �~.l #$EADDEADA,(a0)+   �~.l #$ADDEADAD,(a0)+   �~.l #$DAEAD987,(a0)+   �~.l #$DEADDEAD,(a1)+   �~.l #$EADDEADA,(a1)+   �~.l #$ADDEADAD,(a1)+   �~.l #$DAEAD987,(a1)+ �   �=.w #16,A0 �   �~.l A0,D0 'l1 ��.l (A7)+,A0-A1/A6  ��  'chipm    �~.l mempoolchip,a0    �y AllocPooled(a6)    ��.l D0     �H.w JL_0_25C    ��.l D0,A0    ��.l memlist(PC),A1     �@.l #4,a1     !addtail    ;MOVE.l $4(A1),(A0)    ;MOVE.l A0,$4(A1)    �~.l (A7)+,12(A0)  ;was 4    �~.l #"ChIp",8(a0)  � #mungwall=1    �~.l a0,a1    ��.w #16,a1    �=.l 4(a0),a1    �=.w #16,a0    ;MOVE.l #$DEADDEAD,(a0)+    �~.l 28(a7),(a0)+    �~.l #$EADDEADA,(a0)+    �~.l #$ADDEADAD,(a0)+    �~.l #$DAEAD987,(a0)+    �~.l #$DEADDEAD,(a1)+    �~.l #$EADDEADA,(a1)+    �~.l #$ADDEADAD,(a1)+    �~.l #$DAEAD987,(a1)+   �    �=.w #16,A0  �    �~.l A0,D0    ��.l (A7)+,A0-A1/A6   �� AJL_0_F0:                           ;init .codeinit   �[.l memlist   �~.l d2,-(a7) � #poolmem=1    �~.l $4,a6    �~.l #$10000,d0    �~.l #32768,d1    �~.l #32768,d2    �y -$2b8(a6)    �~.l d0,mempool     ��.l D0    �H.w JL_0_25C    �~.l #$10002,d0    �~.l #32768,d1    �~.l #32768,d2    �y -$2b8(a6)    �~.l d0,mempoolchip     ��.l D0    �H.w JL_0_25Cc    �\.l #"OuT ",AL_0_268  �P 'l10  �~.b #1,debug 'l10 �    �Y.w AJL_0_1EA   �~.l #AJL_0_112,D0   �~.l (a7)+,d2   �[.w AL_0_EE  �� mempool: �%.l 0 mempoolchip: �%.l 0   .codefinit  standardfreecode AJL_0_108: � #mungwall=0    �Y.w AJL_0_218    �P.w AJL_0_108 � � #poolmem=1    �~.l a6,-(a7)    �~.l $4,a6    �~.l mempool,a0    �y -702(a6)    �~.l mempoolchip,a0    �y -702(a6)    �~.l (a7)+,a6  �  ��  AJL_0_112:  �X.w AJL_0_16A  JL_0_116:  �X.w AJL_0_19E  JL_0_11A:  �X.w AJL_0_136  JL_0_11E:  �X.w AJL_0_150  JL_0_122: .findglob   ��.l (A0)    �H.w JL_0_12E   ��.l (A0),A0  �X.w JL_0_122  JL_0_12E:   �~.l A0,memlist  ��  AJL_0_136: .globalloc   �~.l A0,-(A7)   ��.l memlist(PC),A0   �~.l A0,-(A7)    �Y.w findglob    �Y.w AJL_0_16A   �~.l (A7)+,memlist   ��.l (A7)+,A0  ��  AJL_0_150: .globfree   �~.l A0,-(A7)   ��.l memlist(PC),A0   �~.l A0,-(A7)    �Y.w findglob    �Y.w AJL_0_19E   �~.l (A7)+,memlist   ��.l (A7)+,A0  ��  AJL_0_16A: .localloc:;allocate some memory in the local region!   ��.l A0-A1/A6,-(A7)  � #mungwall=1  �=.l #16+16*2,d0  �   �=.l #18,D0                  ;2 bytes must be larger or field fails  �   �~.l D0,-(A7)   ��.l _SysBase,A6   �y -$84(a6) _AllocMem SET -$C6 � #poolmem=1   �W #1,d1   �P 'chipm   �~.l mempool,a0   �y AllocPooled(a6) �    �y _AllocMem(A6) �   ��.l D0    �H.w JL_0_25C   ��.l D0,A0   ��.l memlist(PC),A1   �@.l #4,a1   !addtail ;  MOVE.l $4(A1),(A0) ;  MOVE.l A0,$4(A1) ;  MOVE.l d0,8(a1)   �~.l (A7)+,12(A0);was 4 � #mungwall=1   �~.l a0,a1   ��.w #16,a1   �=.l 12(a0),a1   ;size of mem   �=.w #16,a0   ;MOVE.l #$DEADDEAD,(a0)+   �~.l 28(a7),(a0)+   �~.l #$EADDEADA,(a0)+   �~.l #$ADDEADAD,(a0)+   �~.l #$DAEAD987,(a0)+   �~.l #$DEADDEAD,(a1)+   �~.l #$EADDEADA,(a1)+   �~.l #$ADDEADAD,(a1)+   �~.l #$DAEAD987,(a1)+ �   �=.w #16,A0 �   �y -$8a(a6)   �~.l A0,D0   ��.l (A7)+,A0-A1/A6  �� 'chipm   �~.l mempoolchip,a0   �y AllocPooled(a6)   ��.l D0    �H.w JL_0_25C   ��.l D0,A0   ��.l memlist(PC),A1   �@.l #4,a1   !addtail   ;MOVE.l $4(A1),(A0)   ;MOVE.l A0,$4(A1)   �~.l (A7)+,12(A0)   ;was 4   �~.l #"ChIp",8(a0) � #mungwall=1   �~.l a0,a1   ��.w #16,a1   �=.l 12(a0),a1   �=.w #16,a0   ;MOVE.l #$DEADDEAD,(a0)+   �~.l 28(a7),(a0)+   �~.l #$EADDEADA,(a0)+   �~.l #$ADDEADAD,(a0)+   �~.l #$DAEAD987,(a0)+   �~.l #$DEADDEAD,(a1)+   �~.l #$EADDEADA,(a1)+   �~.l #$ADDEADAD,(a1)+   �~.l #$DAEAD987,(a1)+  �   �=.w #16,A0 �   �y -$8a(a6)   �~.l A0,D0   ��.l (A7)+,A0-A1/A6  ��                                                                                                                                  AJL_0_19E: .locfree    ;a1=mem, d0=len(ignored!)   ��.l D1/A0/A6,-(A7)   ;MOVE.l $1c(a7),-(a7)  ;put call address to stack so wi|eout stacktrace show correct call address   �~.l memlist(PC),D1    �H.w JL_0_1C6   �\.l #0,a1    �H JL_0_1C6 � #mungwall=1   ��.w #16+16,a1 �   ��.w #16,A1 �   ��.l D1,A0   �@.w #4,A0   ��.l _SysBase,A6   �y -$84(a6) JL_0_1B0:   �~.l 4(a1),a0   �^.l (A0),A1    �P.w errormem    ��.l D1,A0     �~.l 12(A1),D0    �@.w #4,A0    �~.l a1,-(a7)    !remove    �~.l (a7)+,a1   ;MOVE.l (A1),(A0) � #poolmem=1 _FreeMem SET -$D2 � #mungwall=1   �~.l a1,a0   �=.w #20,a0   ;CMP.l #$DEADDEAD,(a0)+   ;BNE munghit1   �\.l #$EADDEADA,(a0)+   �P munghit1   �\.l #$ADDEADAD,(a0)+   �P munghit1   �\.l #$DAEAD987,(a0)+   �P munghit1 mungr1: �\.l #$34,d0   �N 'l2   �~.l #$abfeefcd,(a0)+   �~.l #$bcdefefe,(a0)+   'l2   �~.l a1,a0   ��.w #16,a0   �=.l d0,a0   �\.l #$DEADDEAD,(a0)+   �P munghit4   �\.l #$EADDEADA,(a0)+   �P munghit4   �\.l #$ADDEADAD,(a0)+   �P munghit4   �\.l #$DAEAD987,(a0)+   �P munghit4 mungr4 �  �~.l mempool,a0   �\.l #"ChIp",8(a1)   �P 'l1   �~.l mempoolchip,a0 'l1:  �y FreePooled(a6) �    �y _FreeMem(A6) � JL_0_1C6b:    �y -$8a(a6) JL_0_1C6:   ;ADDQ.l #4,a7   ��.l (A7)+,D1/A0/A6  ��  � #mungwall=1 .munghit1  �Y low  �X mungr1 .low:  ��.l d0-d1/a0-a1,-(a7)  �~.l $0,a0 ;show enforcer hit  �~.l a1,a0  �=.w #16,a0  �~.l (a0),a0  �� #30,d0 'l1: �\.w #$4e41,-(a0)  �b d0,'l1  ��.w d0  �O 'l2   �~.l 2(a0),d0  �=.l #9,d0  �� #0  �X 'l3 'l2: �~.l #mungtext,d0  ��.b debug  �P 'l10  �� #11  �X 'l3 'l10: �� #0 'l3: ��.l (a7)+,d0-d1/a0-a1  �� .high: ��.l d0-d1/a0-a1,-(a7)  �~.l #mungtext2,d0  �� #0  ��.l (a7)+,d0-d1/a0-a1  �� munghit2  �Y low  �X mungr2 munghit3  �Y low  �X mungr3 munghit4  �Y low  �X mungr4   �  JL_0_1CC:   �~.l (A0),D1   ��.l D1,A0    �P.w JL_0_1B0   �~.w AL_0_EE(PC),D1    �P.w JL_0_1C6   ��.w AL_0_EE errormem: �~.l #AL_0_277,D0  �� #$0  �X JL_0_1C6b AJL_0_1EA: .newmem:;create a new node (for local stuff)   ��.l D0-D1/A0-A1/A6,-(A7)   ��.l #20,D0   ��.l #$01,D1   ��.l _SysBase,A6 � #poolmem=1   �~.l mempool,a0    �y AllocPooled(a6) �    _AllocMem SET -$C6    �y _AllocMem(A6) �   ��.l D0    �H.w JL_0_25Cb   ��.l D0,A0   �~.l memlist(PC),(A0)   �~.l A0,memlist   �~.l #$e0e0e0e0,16(a0)   ;CLR.l $4(A0)   �@.l #4,a0   !init   ��.l (A7)+,D0-D1/A0-A1/A6  ��  AJL_0_218: .freelast:;free up most recent node, return eq=1 if none!   ��.l D0-D1/A0-A2/A6,-(A7)   ��.l _SysBase,A6   �~.l memlist(PC),D0   �H.w JL_0_256   ��.l D0,A1   �~.l (A1),memlist   �~.l a1,-(a7)   �~.l 4(a1),a2 JL_0_23C:   �\.l #$0,(a2)   �H.w JL_0_254   ��.l A2,A1   �\.l #52,12(a2)   �L 'nopool   �\.l #"lIsT",36+16(a1)          ;free pool from lists   �P 'nopool   �\.w #1,6+16(a1)   �P 'nopool   �~.l 0+16(a1),a0   �\.l #0,a0   �H 'nopool   �~.l a1,-(a7)   �y -702(a6)   �~.l (a7)+,a1 'nopool:  �~.l 12(A2),D0 ;was 4   ��.l (A2),A2 _FreeMem SET -$D2 � #poolmem=1 � #mungwall=1   �~.l a1,a0   �=.w #20,a0   ;CMP.l #$DEADDEAD,(a0)+   ;BNE munghit2   �\.l #$EADDEADA,(a0)+   �P munghit2   �\.l #$ADDEADAD,(a0)+   �P munghit2   �\.l #$DAEAD987,(a0)+   �P munghit2 mungr2: �\.l #$34,d0   �N 'l2   �~.l #$abfeefcd,(a0)+   �~.l #$bcdefefe,(a0)+   'l2   �~.l a1,a0   ��.w #16,a0   �=.l d0,a0   �\.l #$DEADDEAD,(a0)+   �P munghit3   �\.l #$EADDEADA,(a0)+   �P munghit3   �\.l #$ADDEADAD,(a0)+   �P munghit3   �\.l #$DAEAD987,(a0)+   �P munghit3 mungr3 �   �~.l mempool,a0   �\.l #"ChIp",8(a1)   �P 'l1   �~.l mempoolchip,a0 'l1   �y FreePooled(a6)  �    �y _FreeMem(A6)  �  ��.l (a2)  �P.w JL_0_23C  JL_0_254:   �~.l (a7)+,a1   ��.l #20,D0   � #poolmem=0   _FreeMem SET -$D2    �y _FreeMem(A6)   �    �~.l mempool,a0    �y FreePooled(a6)   �                                                                                                                                                                                                                                                              ��.l #-$01,D0 JL_0_256:   ��.l (A7)+,D0-D1/A0-A2/A6  �� debug: �%.w 0 JL_0_25Cb: � #return0=1   ��.l (A7)+,D0-D1/A0-A2/A6   �� �   �~.l #AL_0_268,D0  �� #$0 �  JL_0_25C: � #return0=1   �@.l #4,a7   �y -$8a(a6)   ��.l (A7)+,A0-A1/A6   �� �   �~.l #AL_0_268,D0  �� #$0 � JL_0_25Cc: � #return0=1   �� �   �~.l #AL_0_268,D0  �� #$0 �  AL_0_268:   �%.b "OuT of Memory!",0 AL_0_277:   �%.b "Unable to Free Memory (Hint: program overide illegal mem)",0,0 mungtext  �%.b "Mungwall Hit",0 globfreestate: �%.b 0 mungtext2  �%.b "Mungwall Hit Upper Bound",0   �%.b "p",0   �%.b "Nu",0   �%.b $01 ;.   �' 