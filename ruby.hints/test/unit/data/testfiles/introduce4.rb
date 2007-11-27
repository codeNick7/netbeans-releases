def blocktest
  [4,5,6].each do |foo|
     x = 50
     y = 30
     x = x+y
  end #firstblock
  beforeblock = 50
  notusedinblock = 10
  usedinblock = 20
  [10,11,12].each do |bar|
    z = 50
    q = 60
    [1,2,3].each do |foo|
       x = 51
       y = 30
       x = x+y
       puts y+q
       z = z+50
       puts beforeblock
       readlater = 50
       notreadlater = 60
       usedinblock = 20
    end #block
    puts z
  end #outerblock
  puts y # calls a method, y is not seen from the block
  puts usedinblock
  [7,8,9].each do |foo|
      notreadlater = 50
      puts readlater
  end #lastblock
end

