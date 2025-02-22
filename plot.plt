# plot_script.plt

#set terminal pngcairo size 800,600
#set output "output.png"
#set xrange [0:1]
#set yrange [0:1]
set title "График функции при h=0.001 и tau=0.001 (Java)"
set xlabel "x"
set ylabel "u"
#set ylabel "z"
set grid
#set dgrid3d 20, 20  # Интерполяция сетки
#set pm3d          # Включает цветовую карту

plot "progonka2.txt" with lines lw 3 title "численное решение при t=0.2",\
"progonka5.txt" with lines lw 3 title "численное решение при t=0.5",\
"progonka8.txt" with lines lw 3 title "численное решение при t=1",\
#"progonkaTest.txt" with lines lw 3 title "Test"
pause -1